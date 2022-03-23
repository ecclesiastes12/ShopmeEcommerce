$(document).ready(function(){

	//event handler for add to cart button
	$("#buttonAdd2Cart").on("click", function(evt){
		//function call
		addToCart();
	});
});	

	addToCart =() => {
		//get the quantity value from the text field in the quantity_control.html
		// which is th:id="'quantity' + ${productId}"
		//NB product id is set in product_detail.html thus productId = "[[${product.id}]]";
		quantity = $("#quantity" + productId).val();
		
		//context path is set in in product_detail.html thus contextPath = "[[@{/}]]";
		url = contextPath + "cart/add/" + productId + "/" + quantity;
		
		//ajax call
		$.ajax({
			type: "POST",
			url: url,
			beforeSend: function(xhr){
				xhr.setRequestHeader(csrfHeaderName, csrfValue);
			}
		}).done(function(response){
			showModalDialog("Shopping Cart", response);
		}).fail(function(){
			showErrorModal("Error while adding product to shopping cart. ");
		});
	}
