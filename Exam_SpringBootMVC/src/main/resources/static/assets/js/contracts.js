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

    // Search form handling
    $('searchForm').on('submit', function(event) {
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

				if (response.indexOf("No results found") !== -1) {
					tableBody.html('<tr><td colspan="8" class="text-center">No results found</td></tr>');
				} else {
					tableBody.append(response);
				}
            },
            error: function(xhr, status, error) {
                alert("An error occurred while fetching contracts.");
            }
        });
    });
});
