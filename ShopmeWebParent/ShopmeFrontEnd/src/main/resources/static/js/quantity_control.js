$(document).ready(function(){
	//event handler for plus and minus button
	$(".linkMinus").on("click",function(evt){
		evt.preventDefault();
		
		//$(this) refers to hyperlink of the button
		//attr("pid") grabs the attribute id of the button
		productId = $(this).attr("pid");
		
		//from the value of productId we can get the input text with quantity and product id
		//thus th:id="'quantity' + ${productId}" in quantity_control.html
		quantityInput = $("#quantity" + productId);
		
		//new quantity value when the minus button is pressed. Thus decrease the number by 1
		
		newQuantity = parseInt(quantityInput.val()) - 1;
		
		//check if new quantity value is greater than 0
		if(newQuantity > 0){
			//update the quantity value
			quantityInput.val(newQuantity);
		}else{
			//show warning message
			showWarningModal("Minimum quantity is 1");
			
		}
	});

	$(".linkPlus").on("click",function(evt){
		evt.preventDefault();
		productId = $(this).attr("pid");
		
		//from the value of productId we can get the input text with quantity and product id
		//thus th:id="'quantity' + ${productId}" in quantity_control.html
		quantityInput = $("#quantity" + productId);
		
		//new quantity value when the plus button is pressed. Thus increase the number by 1
		
		newQuantity = parseInt(quantityInput.val()) + 1;
		
		//check if new quantity value is greater than 0
		if(newQuantity <= 5){
			//update the quantity value
			quantityInput.val(newQuantity); 
		}else{
			//show warning message
			showWarningModal("Maximum quantity is 5");
		}	
	});
});