$(document).ready(function () {
    var csrfValue = document.cookie.substr(5);
    document.getElementById("csrf").value = csrfValue;
});

$(".logout-btn").on("click", function(e) {
    e.preventDefault();
    $.ajax({
        type: "post",
        url: "/logout",
        success: function(response) {
            $("html").html(response);
        },
        error : function(xhr) {
            alert("Request failed with error "+ xhr.responseText);
        }
    });
});

