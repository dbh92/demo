var pass = document.getElementById("pass"),
    re_pass = document.getElementById("re_pass");

function checkinput() {
    if (pass.value.length < 6 || pass.value.length > 20) {
        pass.setCustomValidity("Password must be 6-20 characters");
    } else if (pass.value != re_pass.value) {
        re_pass.setCustomValidity("Re-password don't match");
    } else {
        re_pass.setCustomValidity("");
    }
}

pass.onchange = checkinput;
re_pass.onkeyup = checkinput;
