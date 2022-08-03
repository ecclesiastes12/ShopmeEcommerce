function clearFilter(){
	window.location = moduleURL;
}

function showDeleteConfirmModal(link, entityName){
	entityId = link.attr("entityId");
	$("#yesButton").attr("href", link.attr("href"));
	$("#confirmText").text("Are you sure you want to delete " + entityName + " ID " + entityId + " ?");

	$("#confirmModal").modal();
}

/*
 function showDeleteConfirmModal2(link, entityName, customerFullName){
	entityId = link.attr("entityId");
	$("#yesButton").attr("href", link.attr("href"));
	$("#confirmText").text("Are you sure you want to delete  this" + entityName + " ID " + entityId + ": " + customerFullName + " ?");

	$("#confirmModal").modal();
}
*/