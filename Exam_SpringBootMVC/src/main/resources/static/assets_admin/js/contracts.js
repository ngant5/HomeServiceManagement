$(document).ready(function() {
    // Upload file handling
    $("#contractFile").change(function() {
        var fileInput = $(this)[0];
        var uploadButton = $("#uploadButton");
        var fileName = $("#fileName");

        if (fileInput.files.length > 0) {
            fileName.text(fileInput.files[0].name);
            uploadButton.prop("disabled", false);
            uploadButton.removeClass("btn-secondary").addClass("btn-primary");
        } else {
            fileName.text("No file selected");
            uploadButton.prop("disabled", true);
            uploadButton.removeClass("btn-primary").addClass("btn-secondary");
        }
    });

    $("#uploadForm").submit(function(event) {
        event.preventDefault();
        var formData = new FormData();
        var fileInput = $("#contractFile")[0].files[0];
        formData.append("contractFile", fileInput);
        var contractId = $("#contractId").val();
        var uploadUrl = "/admin/contracts/" + contractId + "/uploadFile";  

        $.ajax({
            url: uploadUrl,
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            success: function(response) {
                $("#uploadMessage").html("<div class='alert alert-success'>" + response + "</div>");
                setTimeout(function() {
                    location.reload();
                }, 2000);
            },
            error: function(xhr, status, error) {
                var errorMessage = xhr.responseText || "An error occurred";
                $("#uploadMessage").html("<div class='alert alert-danger'>" + errorMessage + "</div>");
            }
        });
    });

	$('#startDate, #endDate').on('change', function() {
	        var startDate = $('#startDate').val();
	        var endDate = $('#endDate').val();
			console.log("Start Date:", startDate);  // Log giá trị của startDate
			        console.log("End Date:", endDate);

	        // If startDate is chosen, set min value for endDate to startDate
	        if (startDate) {
	            $('#endDate').attr('min', startDate);
				console.log("End Date min set to:", startDate); 
	        }

	        // Enable searchButton only when both startDate and endDate are selected
	        if (startDate && endDate) {
	            $('#searchButton').prop('disabled', false);
				console.log("Both startDate and endDate selected. Search button enabled.");

	        } else {
	            $('#searchButton').prop('disabled', true);
				console.log("Either startDate or endDate is missing. Search button disabled.");

	        }
	    });
    // Search form handling
    $('#searchForm').on('submit', function(event) {
        event.preventDefault();
        let startDate = $('#startDate').val();
        let endDate = $('#endDate').val();
        let page = 1;
        let size = 10;

        $.ajax({
            url: '/admin/contracts/list',
            type: 'GET',
            data: { startDate: startDate, endDate: endDate, page: page, size: size },
            success: function(response) {
				let tableBody = $('#contractTableBody');
				tableBody.empty();  
				tableBody.html(response);
				            
			},
            error: function(xhr, status, error) {
                alert("An error occurred while fetching contracts.");
            }
        });
    });
});