//code to show list of state by the StateRestController class
 //NB you can also use var instead of let to declare local variable
 	let dropDownCountry;
 	let dataListStates;
 	let fieldState;
 	
 	$(document).ready(function(){
 		//select the country id from the list of countries in the dropdown on the form
 		dropDownCountry = $("#country"); //country here is the th:field selector value which represent country id
 	
 		dataListStates = $("#listStates"); //reference id of datalist in states
 		
 		fieldState = $("#state");
 		
 		//change event handler on dropdown country
 		dropDownCountry.on("change", function(){
 			
 			//function call
 			loadState4Country();
 			fieldState.val("").focus();
 		});
 		
 		loadState4Country();
 	});


	//method to load state for country
	loadState4Country = () => {
	
		//select the selected country option
		//#country refers to the id of the selected country in the dropdown list of countries
		selectedCountry = $("#country option:selected");
		
		//grab the id of the selected country
		countryId = selectedCountry.val();
		
		//url for StateRestController
		url = contextPath + "states/list_by_country/" + countryId;
	
		//ajax get call restful web service
		$.get(url, function(responseJSON){
			//clears or empty the content of dataListState
			dataListStates.empty();
			
			//iterate through each state object in the responseJson
			//with parameter 1 as index of the current element and parameter 2 as state object
			$.each(responseJSON, function(index, state){
			
				//generate html option element and append it to the dataListState
				//Since the state name is stored in text( <input type="text" th:field="*{state}" class="form-control" ...)
				//the vale for the option should be val(state.name) but not val(state.id)
				$("<option>").val(state.name).text(state.name).appendTo(dataListStates);
			});
		}).fail(function() {
			alert('failed to connect to the server!');
		});
	
	}
 
 	
 	//code move to common_modal.js
 	/* function to show modal dialog with warning message*/
 	/*function showModalDialog(title,message){
 	$("#modalTitle").text(title);
 	$("#modalBody").text(message);
 	$("#modalDialog").modal();
 	}
 	*/
 	
 	/*functon for showing error message of the modal dialog*/
 	/*function showErrorModal(message){
 	showModalDialog("Error", message);
 	}*/
 	
 	/*functon for showing warning message of the modal dialog*/
 	/*function showWarningModal(message){
 	showModalDialog("Warning", message);
 	}*/