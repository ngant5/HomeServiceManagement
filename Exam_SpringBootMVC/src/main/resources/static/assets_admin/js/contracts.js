$(document).ready(function() {
    // Upload file handling
     $("input[type='file']").change(function() {
        var fileInput = $(this)[0];
		var form = $(this).closest("form");
		var formId = form.attr("id");
		var contractId = $(this).closest("form").find("input[name='contractId']").val();
		var uploadButton = $("#" + formId + " button[type='submit']"); 
		var fileName = $("#" + formId + " span");

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

    $("form[id^='uploadForm']").submit(function(event) {
        event.preventDefault();
		var form = $(this); 
		var formId = form.attr("id"); 

		var formData = new FormData();
		var fileInput = $(this).find("input[type='file']")[0].files[0];
		var contractId = $(this).find("input[name='contractId']").val();

		    // Kiểm tra tệp và contractId có hợp lệ không
		    if (!fileInput || !contractId) {
		        alert("Contract ID or file is missing");
		        return;
		    }

		    formData.append("contractFile", fileInput); // Thêm tệp vào formData
		 

        var uploadUrl = "/admin/contracts/" + contractId + "/uploadFile";  

        $.ajax({
            url: uploadUrl,
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            success: function(response) {
				form.find("#uploadMessage").html("<div class='alert alert-success'>" + response + "</div>");
				                setTimeout(function() {
				                    location.reload();
				                }, 1000);
            },
            error: function(xhr, status, error) {
                var errorMessage = xhr.responseText || "An error occurred";
                $("#" + formId + " #uploadMessage").html("<div class='alert alert-danger'>" + errorMessage + "</div>");
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