
//variable declaration
decimalSeperator = decimalPointType == 'COMMA' ? ',' : '.';
thousandsSeperator = thousandsPointType == 'COMMA' ? ',' : '.';

$(document).ready(function(){
	//event handler for plus and minus button
	$(".linkMinus").on("click",function(evt){
		evt.preventDefault();
		decreaseQuantity($(this));
		
	});

	$(".linkPlus").on("click",function(evt){
		evt.preventDefault();
		increaseQuantity($(this));
			
	});
	
	//event handler for remove/trash button
	$(".linkRemove").on("click",function(evt){
		evt.preventDefault();
		//url = $(this).attr("href");
		//alert(url);
		removeProduct($(this));
		
	});
});

//function that decrease quantity
decreaseQuantity = (link) =>{

	//$(this) refers to hyperlink of the button
	//attr("pid") grabs the attribute id of the button
	productId = link.attr("pid");
	
	//from the value of productId we can get the input text with quantity and product id
	//thus th:id="'quantity' + ${productId}" in quantity_control.html
	quantityInput = $("#quantity" + productId);
	
	//new quantity value when the minus button is pressed. Thus decrease the number by 1
	
	newQuantity = parseInt(quantityInput.val()) - 1;
	
	//check if new quantity value is greater than 0
	if(newQuantity > 0){
		//update the quantity value
		quantityInput.val(newQuantity);
			updateQuantity(productId, newQuantity);
	}else{
		//show warning message
		showWarningModal("Minimum quantity is 1");
		
	}
}

increaseQuantity = (link) =>{
	productId = link.attr("pid");
		
		//from the value of productId we can get the input text with quantity and product id
		//thus th:id="'quantity' + ${productId}" in quantity_control.html
		quantityInput = $("#quantity" + productId);
		
		//new quantity value when the plus button is pressed. Thus increase the number by 1
		
		newQuantity = parseInt(quantityInput.val()) + 1;
		
		//check if new quantity value is greater than 0
		if(newQuantity <= 5){
			//update the quantity value
			quantityInput.val(newQuantity); 
			updateQuantity(productId, newQuantity);
		}else{
			//show warning message
			showWarningModal("Maximum quantity is 5");
		}
}


//function that updated quantity
updateQuantity = (productId, quantity) =>{
	 
	
	//context path is set in in product_detail.html thus contextPath = "[[@{/}]]";
	url = contextPath + "cart/update/" + productId + "/" + quantity;
	
	//ajax call
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(updatedSubtotal){
		
		//showModalDialog("Shopping Cart", response);
		updateSubtotal(updatedSubtotal, productId);
		updateTotal();
	}).fail(function() {
		showErrorModal("Error while updating product quantity.");
	});	
}

/*
//function to update the subtotal
function updateSubtotal(updatedSubtotal, productId) {
	//format updated subtotal
	//NB without jquery.number.min.js library this will not work
	formattedSubtotal = $.number(updatedSubtotal, 2);
	
	//$("#subtotal" + productId).text(updatedSubtotal);
	$("#subtotal" + productId).text(formattedSubtotal);
}
*/

//function to update the subtotal modified
function updateSubtotal(updatedSubtotal, productId) {
	//format updated subtotal
	//NB without jquery.number.min.js library this will not work
	//formattedSubtotal = $.number(updatedSubtotal, 2);
	
	//$("#subtotal" + productId).text(updatedSubtotal);
	//$("#subtotal" + productId).text(formattedSubtotal);
	$("#subtotal" + productId).text(formatCurrency(updatedSubtotal));
}

//function that update estimated total
/*function updateTotal() {
	total = 0.0;
	
	//iterate through each subtotal
	$(".subtotal").each(function(index, element) {
		//replaceAll(",", "") replace the default thousand seperator by empty string
		total += parseFloat(element.innerHTML.replaceAll(",", ""));
	});
	
	formattedTotal = $.number(total, 2);
	$("#total").text(formattedTotal);
}
*/

/*
//function that update estimated total modified
function updateTotal() {
	total = 0.0;
	productCount = 0;
	//iterate through each subtotal
	$(".subtotal").each(function(index, element) {
		productCount++;
		
		//replaceAll(",", "") replace the default thousand seperator by empty string
		total += parseFloat(element.innerHTML.replaceAll(",", ""));
	});
	
	//check if productCount is less than 1. meaning if shopping cart is empty
	if(productCount < 1){
		//error message
		showEmptyShoppingCart();
	}else{
		//NB without jquery.number.min.js library this will not work
		formattedTotal = $.number(total, 2);
		$("#total").text(formattedTotal);
	}
}	
*/

//function that update estimated total modified again
function updateTotal() {
	total = 0.0;
	productCount = 0;
	//iterate through each subtotal
	$(".subtotal").each(function(index, element) {
		productCount++;
		
		//replaceAll(",", "") replace the default thousand seperator by empty string
		//total += parseFloat(element.innerHTML.replaceAll(",", ""));
		total += parseFloat(clearCurrencyFormat(element.innerHTML));
	});
	
	//check if productCount is less than 1. meaning if shopping cart is empty
	if(productCount < 1){
		//error message
		showEmptyShoppingCart();
	}else{
		//NB without jquery.number.min.js library this will not work
		//formattedTotal = $.number(total, 2);
		//$("#total").text(formattedTotal);
		
		$("#total").text(formatCurrency(total));
	}
	
}

function showEmptyShoppingCart(){
	$("#sectionTotal").hide();
	$("#sectionEmptyCartMessage").removeClass("d-none");
}

//remove function
function removeProduct(link){
	//remove url in shopping cart rest controller
	url = link.attr("href");
	
	//ajax call
	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response){
		//alert(response);
		//grab the row number from the link thus status.count
		rowNumber = link.attr("rowNumber");
		removeProductHTML(rowNumber);
		
		//update estimated total after removing a product from the cart
		updateTotal();
		updateCountNumbers();
		showModalDialog("Shopping Cart", response);
	}).fail(function() {
		showErrorModal("Error while removing product.");
	});	
}

//function to remove product from the html view in the cart when cart item is deleted
function removeProductHTML(rowNumber){
	$("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();
}

//function that update the text of status.count in shopping_cart.html when 
//item is deleted from the cart
function updateCountNumbers(){
	$(".divCount").each(function(index, element){
		//updates the html
		element.innerHTML = "" + (index + 1);
	});
}

//function that format currency numbers according currency settings
function formatCurrency(amount){
	//NB without jquery.number.min.js library this will not work
	return $.number(amount, decimalDigits,decimalSeperator,thousandsSeperator);
}

//function that clear currency format
function clearCurrencyFormat(numberString){
	result = numberString.replaceAll(thousandsSeperator, ""); //replace thousandsSeperator by empty string
	return result.replaceAll(decimalSeperator, "."); //replace decimalSeperator by default point type
}

