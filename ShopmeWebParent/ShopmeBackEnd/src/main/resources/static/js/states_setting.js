
//variable declaration

var buttonLoad4States;
var dropDownStates;
var dropDownCountry4States;


var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;


$(document).ready(function(){
	//assign value to the above declared variables
	buttonLoad4States = $("#buttonLoadCountriesForStates");
	dropDownCountry4States = $("#dropDownCountriesForStates");
	dropDownStates = $("#dropDownStates");
	//select to get the button elements
	buttonAddState = $("#buttonAddState");
	buttonUpdateState = $("#buttonUpdateState");
	buttonDeleteState = $("#buttonDeleteState");
	
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");
	
		
	//event handler for button load
	buttonLoad4States.click(function(){
		//function call
		loadCountries4States();
		
	});
	
	//change event handler for dropDownCountry4States
	dropDownCountry4States.on("change", function() {
		//function call	
		loadStates4Country();
	});
	
	//change event handler for dropdown states
	dropDownStates.on("change", function(){
		changeFormStateToSelectedState();//function call
	})
	
	//button to add new country
	buttonAddState.click(function(){
		//check  the caption on the button
		if(buttonAddState.val() == "Add"){
			addState();//function call
		}else{
			changeFormStateToNew(); //function call
		}
	});
	
	//button to update or edit country
	buttonUpdateState.click(function(){
		updateState(); // function call
	});
	
	//button to delete country
	buttonDeleteState.click(function(){
		deleteState();
	});
	
	
});

//function to delete country
function deleteState(){
	//grab the country id from the dropdown list
	//optionValue = dropDownCountry4States.val();
	stateId = dropDownStates.val(); // country id is the first element in the optionValue
	
	//url from CountryRestController
	url = contextPath + "states/delete/" + stateId; //url containing id of the country to be deleted
	
	//Since @DeleteMapping... is used in the delete function in the CountryRestController.
	//We cannot use the ajax get functon to delete a country but rather ajax delete
	/*
	$.get(url, function(){
		//remove the selected item thus deleted country from list of countries in the dropdown list
		$("#dropDownStates option[value ='" + stateId +"']").remove();
		
		//change the form state to new after country has been deleted
		changeFormStateToNew(); // method call
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
		$("#dropDownStates option[value ='" + stateId +"']").remove();
		
		//change the form state to new after country has been deleted
		changeFormStateToNew(); // method call
		showToastMessage("The state has been deleted");
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

//function to update country
function updateState(){
	if(!validateFormState()) return;
	
		//url from CountryRestController
	url = contextPath + "states/save";
	
	//grabs the values in the textfield
	stateName = fieldStateName.val(); 
	
	
	//gets the id for the state to be updated
	//the state to be updated contains state id and name 
	stateId = dropDownStates.val(); //first element contains the country id
	 
	//select country which states is to be updated
	selectCountry = $("#dropDownCountriesForState option:selected"); 
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	 
	//set json data to send a request to the server
	jsonData = {id: stateId, name: stateName, country: {id: countryId, name:countryName}};
	
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
		
	}).done(function(stateId){//function to be executed after the ajax call has been successfully executed
		//update the selected text in the dropdown list 
		$("#dropDownStates option:selected").text(stateName);
		
		 
		showToastMessage("The state has been updated");
		
		//change form state after state has been updated
		changeFormStateToNew(); // method call 
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

validateFormState = () => {
	//validates form formCountry
	formState = document.getElementById("formState");
	//checks form validity
	if(!formState.checkValidity()){
		formState.reportValidity(); //shows validity error messages
		return false;
	}
	
	return true
	
}

//method to add a new country
function addState(){
	if(!validateFormState()) return;
	
	//url from StateRestController
	url = contextPath + "states/save";
	
	//grabs the values in the textfield
	stateName = fieldStateName.val(); 
	
	//grab id of selected country before new state is added
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val(); //grab the country id from the selected country in the dropdown list
	countryName = selectedCountry.text(); //grab the country name which is the text of the selected country element
	 
	//json data to send a request to the server
	jsonData = {name: stateName, country: {id: countryId, name: countryName}};
	
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
		
	}).done(function(stateId){//function to be executed after the ajax call has been successfully executed
		//alert("Newly added country ID:" + countryId);
		
		//function call
		selectNewlyAddedState(stateId, stateName);
		
		
		showToastMessage("The new country has been added");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

//function append the newly added country to the end of list of countries
function selectNewlyAddedState(stateId, stateName){
	//optionValue = countryId + "-" + countryCode;
	
	//html code to show the state at the end of list of states
	//$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry4States);
	$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);
	
	//select dropdown states by id
	//select the option with the value thus optionValue
	//set property of the selected to true
	$("#dropDownStates option[value='" + stateId + "']").prop("selected", true);
	
	//empty the fieldCountryName and fieldCountryCode text fields
	
	fieldStateName.val("").focus();
	
}

//function to change form state when the button new is clicked
function changeFormStateToNew(){
	buttonAddState.val("Add");//changes the button text to add
	labelStateName.text("State/Province Name:"); // change the selected country to country name
	
	//disabled the buttons when button "New" is clicked
	buttonUpdateState.prop("disabled", true); 
	buttonDeleteState.prop("disabled", true);
	
	//clear the textfield
	
	fieldStateName.val("").focus();  //focus() puts focus on the textfield
}

//function to change the state of dropDownStates
function changeFormStateToSelectedState(){
	//change the property buttons when country is loaded in the dropDownStates
	buttonAddState.prop("value", "New"); //set the property button to "New"'
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);
	
	
	labelStateName.text("Selected State/Province:"); //changes the label name when a country is selected
	
	//gets the selected text from the dropdown list
	//NB dropDownStates will populate all states in the texfield 
	//option:selected will populate only the selected item in the dropdownlist in the textfield
	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);
	
	
}

//function to load states
function loadStates4Country(){
	//select dropdown countries by id
	//select the option with the value thus optionValue
	//set property of the selected to true
	//$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);
	
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	//get the id for selected country
	//selectedCountryId = selectedCountry.val().split("-")[0]; 
	selectedCountryId = selectedCountry.val();
	
	//url for list of states by country id
	url = contextPath + "states/list_by_country/" + selectedCountryId;
	
	//ajax call to the server by http request method with a given url.
	// execute call back function with responseJSONse from the server
	$.get(url, function(responseJSON){
		
		//clear the content of the dropDownStates
		dropDownStates.empty();
		
		$.each(responseJSON, function(index, state){
			
			//create html code for option element
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
		});
		
	}).done(function() { //show the in the function if load country is successful
		
		changeFormStateToNew();
		
		//function call
		showToastMessage("All states have been loaded for country " + selectedCountry.text());
		
	}).fail(function(){ //show message in this function if load country fails
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	});
}

//fu nction
function loadCountries4States(){
	//alert("About loading countries...");
	
	//url of the CountryRestController
	url = contextPath + "countries/list";
	
	//ajax call to the server by http request method with a given url.
	// execute call back function with responseJSONse from the server
	$.get(url, function(responseJSON){
		
		//clear the content of the dropDownCountry
		dropDownCountry4States.empty();
		
		//iterate through each country element in responseJSON from the server
		//NB spring will convert list of country object to JSON data. this in done in CountryRestController
		//parameter 1 is the index of the country element
		//parameter 2 is the country object.
		
		$.each(responseJSON, function(index, country){
			
			//get the values of the country objects
			//optionValue = country.id + "-" + country.code;
			
			
			//alert(optionValue);
			
			//create html code for option element
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountry4States);
		});
		
	}).done(function() { //show the in the function if load country is successful
		
		//change the caption of buttonLoad
		buttonLoad.val("Refresh Country List");
		//alert("All countries have been loaded");
		
		//function call
		showToastMessage("All countries for states have been loaded");
		
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