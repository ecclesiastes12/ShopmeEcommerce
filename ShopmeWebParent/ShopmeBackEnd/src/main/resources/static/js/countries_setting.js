
//variable declaration

var buttonLoad;
var dropDownCountry;

var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;

$(document).ready(function(){
	//assign value to the above declared variables
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");
	
	//select to get the button elements
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	
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
});

//function to change the state of dropDownCountry
function changeFormStateToSelectedCountry(){
	
	buttonAddCountry.prop("value", "New"); 
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);
	
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
		
	}).done(function(){ //show the in the function if load country is successful
		
		//change the caption of buttonLoad
		buttonLoad.val("Refresh Country List");
		//alert("All countries have been loaded");
		
		//function call
		showToastMessage("All countries have been loaded");
		
	}).fail(function(){ //show message in this function if load country fails
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	});
	
	//for toast messages
	function showToastMessage(message){
		//select id of toast message from settings.html
		$("#toastMessage")
			.text(message); //set the message
		
		$(".toast").toast("show"); //shows the toast messge
				
	}
}
