/* function to show modal dialog with warning message*/
 	function showModalDialog(title,message){
 	$("#modalTitle").text(title);
 	$("#modalBody").text(message);
 	$("#modalDialog").modal();
 	}
 	
 	
 	/*functon for showing error message of the modal dialog*/
 	function showErrorModal(message){
 	showModalDialog("Error", message);
 	}
 	
 	/*functon for showing warning message of the modal dialog*/
 	function showWarningModal(message){
 	showModalDialog("Warning", message);
 	}