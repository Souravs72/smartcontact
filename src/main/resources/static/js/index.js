const showPassword = () => {
  var x = document.getElementById("myPassword");
  if (x.type === "password") {
    x.type = "text";
  } else {
    x.type = "password";
  }
}

const toggleSideBar = () => {
  if($(".sidebar").is(":visible")) {
    $(".sidebar").css("display", "none");
    $(".content").css("margin-left", "0%");
    $("#expand").css("display", "block");
  } else {
		var x = window.matchMedia("(max-width: 800px)").matches;

		if (x) {
			$(".sidebar").css("display", "block").css("z-index", "1");
			$("#expand").css("display", "none");
		} else {
		 	$(".sidebar").css("display", "block");
			$(".content").css("margin-left", "20%");
			$("#expand").css("display", "none");
		}
    
  }
}

function deleteContact(cId) {
	Swal.fire({
	  title: "Are you sure?",
	  text: "This will delete your contact!",
	  icon: "info",
	  showCancelButton: true,
	  confirmButtonColor: "#dc3545",
	  cancelButtonColor: "#009698",
	  confirmButtonText: "Yes, delete it!"
	}).then((result) => {
	  if (result.isConfirmed) {
		  window.location="/user/delete/" + cId
	   /* Swal.fire({
	      title: "Deleted!",
	      text: "Your contact has been deleted.",
	      icon: "success"
	    });*/
	  }
	  else {
		  Swal.fire({
			  title:"Cancelled",
			  icon: "error"
		  });
	  }
	});
}