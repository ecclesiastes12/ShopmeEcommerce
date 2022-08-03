
//variable declaration

var buttonLoad;
var dropDownCountry;

var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function(){
	//assign value to the above declared variables
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");
	
	//select to get the button elements
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");
		
	//event handler for button load
	buttonLoad.click(function(){
		//function call
		loadCountries();
		
	});
	
	//change event handler for dropDownCountry
	dropDownCountry.on("change", function() {
		//function call
		changeFormStateToSelectedCountry();
	});
	
	//button to add new country
	buttonAddCountry.click(function(){
		//check  the caption on the button
		if(buttonAddCountry.val() == "Add"){
			addCountry();//function call
		}else{
			changeFormStateToNewCountry(); //function call
		}
	});
	
	//button to update or edit country
	buttonUpdateCountry.click(function(){
		updateCountry(); // function call
	});
	
	//button to delete country
	buttonDeleteCountry.click(function(){
		deleteCountry();
	});
	
	
});

//function to delete country
function deleteCountry(){
	//grab the country id from the dropdown list
	optionValue = dropDownCountry.val();
	countryId = optionValue.split("-")[0]; // country id is the first element in the optionValue
	
	//url from CountryRestController
	url = contextPath + "countries/delete/" + countryId; //url containing id of the country to be deleted
	
	
	//Since @DeleteMapping... is used in the delete function in the CountryRestController.
	//We cannot use the ajax get functon to delete a country but rather ajax delete
	/*
	$.get(url, function(){
		//remove the selected item thus deleted country from list of countries in the dropdown list
		$("#dropDownCountries option[value ='" + optionValue +"']").remove();
		
		//change the form state to new after country has been deleted
		changeFormStateToNewCountry(); // method call
	})
	*/
		$.ajax({
		type: 'DELETE',
		url: url,
		//csrf to pass security check
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
		
	}).done(function(){
		//remove the selected item thus deleted country from list of countries in the dropdown list
		$("#dropDownCountries option[value ='" + optionValue +"']").remove();
		
		//change the form state to new after country has been deleted
		changeFormStateToNewCountry(); // method call
		
		showToastMessage("The country has been deleted");
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

//function to update country
function updateCountry(){
	if(!validateFormCountry()) return; //function call
	
		//url from CountryRestController
	url = contextPath + "countries/save";
	
	//grabs the values in the textfield
	countryName = fieldCountryName.val(); 
	countryCode = fieldCountryCode.val();
	
	//gets the id for the country to be updated
	//the country to be updated contains country id, name and code which has to be split 
	//in order to get the country code
	countryId = dropDownCountry.val().split("-")[0]; //first element contains the country id
	 
	//set json data to send a request to the server
	jsonData = {id: countryId, name: countryName, code: countryCode};
	
	//makes ajax call
	$.ajax({
		type: 'POST',
		url: url,
		//csrf to pass security check
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		//stringify is used to convert json object to string
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
		
	}).done(function(countryId){//function to be executed after the ajax call has been successfully executed
		//update the selected text in the dropdown list 
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		 
		showToastMessage("The country has been updated");
		
		//change form state after country has been updated
		changeFormStateToNewCountry(); // method call 
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

//method to validate formCountry because the form has no submit button
validateFormCountry = () =>{
	//validates form formCountry
	formCountry = document.getElementById("formCountry");
	//checks form validity
	if(!formCountry.checkValidity()){
		formCountry.reportValidity(); //shows validity error messages
		return false;
	}
	
	return true
	
}

//method to add a new country
function addCountry(){
	if(!validateFormCountry()) return; //function call
	//url from CountryRestController
	url = contextPath + "countries/save";
	
	//grabs the values in the textfield
	countryName = fieldCountryName.val(); 
	countryCode = fieldCountryCode.val();
	 
	//json data to send a request to the server
	jsonData = {name: countryName, code: countryCode};
	
	//makes ajax call
	$.ajax({
		type: 'POST',
		url: url,
		//csrf to pass security check
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		//stringify is used to convert json object to string
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
		
	}).done(function(countryId){//function to be executed after the ajax call has been successfully executed
		//alert("Newly added country ID:" + countryId);
		
		//function call
		selectNewlyAddedCountry(countryId, countryCode, countryName);
		
		
		showToastMessage("The new country has been added");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

//function append the newly added country to the end of list of countries
function selectNewlyAddedCountry(countryId, countryCode, countryName){
	optionValue = countryId + "-" + countryCode;
	
	//html code to show the country at the end of list of countries
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
	
	//select dropdown countries by id
	//select the option with the value thus optionValue
	//set property of the selected to true
	$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);
	
	//empty the fieldCountryName and fieldCountryCode text fields
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
	
}

//function to change form state when the button new is clicked
function changeFormStateToNewCountry(){
	buttonAddCountry.val("Add");//changes the button text to add
	labelCountryName.text("Country Name:"); // change the selected country to country name
	
	//disabled the buttons when button "New" is clicked
	buttonUpdateCountry.prop("disabled", true); 
	buttonDeleteCountry.prop("disabled", true);
	
	//clear the textfield
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();  //focus() puts focus on the textfield
}

//function to change the state of dropDownCountry
function changeFormStateToSelectedCountry(){
	
	buttonAddCountry.prop("value", "New"); 
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);
	
	
	labelCountryName.text("Selected Country:"); //changes the label name when a country is selected
	
	//gets the selected text from the dropdown list
	//NB dropDownCountries will populate all countries in the texfield 
	//option:selected will populate only the selected item in the dropdownlist in the textfield
	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);
	
	countryCode = dropDownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode);
	
}

//function
function loadCountries(){
	//alert("About loading countries...");
	
	//url of the CountryRestController
	url = contextPath + "countries/list";
	
	
	$.get(url, function(responseJSON){
		
		//clear the content of the dropDownCountry
		dropDownCountry.empty();
		
		//iterate through each country element in responseJSON from the server
		//NB spring will convert list of country object to JSON data. this in done in CountryRestController
		//parameter 1 is the index of the country element
		//parameter 2 is the country object.
		
		$.each(responseJSON, function(index, country){
			
			//get the values of the country objects
			optionValue = country.id + "-" + country.code;
			
			//alert(optionValue);
			
			//create html code for option element
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
		
	}).done(function() { //show the in the function if load country is successful
		
		//change the caption of buttonLoad
		buttonLoad.val("Refresh Country List");
		//alert("All countries have been loaded");
		
		//function call
		showToastMessage("All countries have been loaded");
		
	}).fail(function(){ //show message in this function if load country fails
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	});
		
}

//for toast messages
	function showToastMessage(message){
		//select id of toast message from settings.html
		$("#toastMessage")
			.text(message); //set the message
		
		$(".toast").toast("show"); //shows the toast messge
			
	}

	


