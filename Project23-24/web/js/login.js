var logged_in_username = "";
var logged_in_password = "";
var logged_in_userType = "";
var logged_in_id = "";
var keeper_uname;
var keeper_price;
var glob_final_price;
var can_register_pet = true;

function loginPOST(){
    let myForm = document.getElementById("login_form");
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => data[key] = value);
    let jsonData = JSON.stringify(data);
    logged_in_username = data["username"];
    logged_in_password = data["password"];
    console.log("logged_in_username: " + logged_in_username);
    console.log("logged_in_password: " + logged_in_password);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'Login');
    xhr.setRequestHeader("Content-type", "aplication/json");
    xhr.send(jsonData);
    
    xhr.onload = function(){
      if(xhr.status === 200){
          const res = JSON.parse(xhr.responseText);
          set_login_page(res);
          
      }else if(xhr.status === 401){
          logged_in_username = "";
          logged_in_password = "";
          logged_in_userType = "";
          logged_in_id = "";
          $('#login_msg').html("Provide a correct username and password");
          $('#login_msg').show();
      }else if(xhr.status !== 200){
          logged_in_username = "";
          logged_in_password = "";
          logged_in_userType = "";
          logged_in_id = "";
          alert('Request failed. Returned status ' + xhr.status);
      }
    };
    
    return false;
    
    
}

function set_login_page(response){
    logged_in_userType = response.userType;
    console.log("logged_in_userType: " + response.userType);
    if(response.userType === "admin"){
        $('#home').hide();
        $('#login').hide();
        $('#register').hide();
        $('#keepers_table').hide();
        $('#logout').append('<a href="index.html" onclick="logout()">Logout</a>');
        $('#login_wrapper').load('jsp/admin_page.jsp');
    }else if(response.userType === "owner"){
        $('#home').hide();
        $('#login').hide();
        $('#register').hide();
        $('#keepers_table').hide();
        $('#logout').append('<a href="index.html" onclick="logout()">Logout</a>');
        $('#profile').append('<a onclick="updateInit()">Profile</a>');
        $('#pet').append('<a onclick="append_pet_buttons()">Pet</a>');
        $('#avail_keepers').append('<a onclick="avail_keep_init()">Pet Keepers</a>');
        $('#bookings').append('<a onclick="bookings_init()">Bookings</a>');
        $('#login_form').hide();
        $('#login_msg').empty();
        $('#login_msg').append('Welcome ' + logged_in_username);
    }else if(response.userType === "keeper"){
        $('#home').hide();
        $('#login').hide();
        $('#register').hide();
        $('#keepers_table').hide();
        
        $('#logout').append('<a href="index.html" onclick="logout()">Logout</a>');
        $('#profile').append('<a onclick="updateInit()">Profile</a>');
        $('#bookings').append('<a onclick="bookings_init()">Bookings</a>');
        $('#reviews').append('<a onclick="keeper_reviews_init()">Reviews</a>');
        $('#login_form').hide();
        $('#login_msg').append('Welcome ' + logged_in_username);
    }
}

function bookings_init(){

    $('#login_msg').empty();
    $('#book_area').empty();
    $('#review_area').empty();
    $('#login_wrapper').load('manage_bookings.html');
    
    bookingPOST();
}

function bookingPOST(){
    let data = {};
    data['username'] = logged_in_username;
    data['userType'] = logged_in_userType;
    let jsonData = JSON.stringify(data);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'ManageBooking');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if (xhr.readyState === 4 && xhr.status === 200){
            show_bookings(xhr.responseText);
            
        } else if (xhr.status !== 200){
            $('#man_book_msg').append("You have no bookings");
        }
    };
}

var review_bid;
var review_ku;

function show_bookings(response){
    
    const jr = JSON.parse(response);
   
    let text = '[{';
    for(var i = 0; i < jr.length; i++){
        text = text + jr[i];
        if(i < jr.length - 1)
            text = text + '},{';
    }
    text = text + '}]';
    const obj = JSON.parse(text);
    
    for(var i = 0; i < jr.length; i++){
        
        if(logged_in_userType === "owner"){

            $('#o_uname').remove();
            $('#bookings_body').append('<td>' + obj[i].username + '</td><td>' + obj[i].fromdate + '</td><td>' + obj[i].todate + '</td><td>' + obj[i].price + '</td><td>' + obj[i].status + '</td>');
            if(obj[i].status === "finished")
                $('#bookings_body').append('<td><button onclick="load_review(\'' + obj[i].booking_id + '\', \'' + obj[i].username + '\')">Review</button></td>');
            else
                 $('#bookings_body').append('<td><button onclick="booking_change_status(\'' + obj[i].booking_id + '\', \'finished\')">Finish</button></td>');
            $('#bookings_body').append('</tr>');

        }else if(logged_in_userType === "keeper"){

            $('#k_uname').remove();
            $('#bookings_body').append('<tr><td>' + obj[i].username + '</td><td>' + obj[i].fromdate + '</td><td>' + obj[i].todate + '</td><td>' + obj[i].price + '</td><td>' + obj[i].status + '</td><td><button onclick="booking_change_status(\'' + obj[i].booking_id + '\', \'accepted\')">Accept</button><button onclick="booking_change_status(\'' + obj[i].booking_id + '\', \'rejected\')">Reject</button></td></tr>');

        }
        
    }
}



function load_review(booking_id, keeper_uname){
    $('#book_area').empty();
    $('#book_area').append('<h3>Reviewing ' + keeper_uname +  '</h3>');
    $('#review_area').load('review_form.html');
    review_ku = keeper_uname;
    review_bid = booking_id;
}

function reviewPOST(){
    
    
    
    let myForm = document.getElementById("review_form");
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => data[key] = value);
    data['booking_id'] = review_bid;
    let jsonData = JSON.stringify(data);
    
    console.log("jsonData: " + jsonData);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'ReviewRegister');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if (xhr.readyState === 4 && xhr.status === 200){
            $('#review_area').empty();
            $('#review_area').append("The review was successful");
        } else if (xhr.status !== 200){
            $('#review_area').append("You already have submitted a review for that booking");
        }
    };
}

function keeper_reviews_init(){
    
    $('#login_msg').empty();
    $('#login_wrapper').load('keeper_reviews.html');
    
    let data = {};
    data['username'] = logged_in_username;
    data['userType'] = logged_in_userType;
    let jsonData = JSON.stringify(data);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'GetReview');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if (xhr.readyState === 4 && xhr.status === 200){
            show_keeper_reviews(xhr.responseText);
        } else if (xhr.status !== 200){
            alert(xhr.status);
        }
    };
    
}


function show_keeper_reviews(response){
    
    const jr = JSON.parse(response);
    
    let text = '[{';
    for(var i = 0; i < jr.length; i++){
        text = text + jr[i];
        if(i < jr.length - 1)
            text = text + '},{';
    }
    text = text + '}]';
    const obj = JSON.parse(text);
    
    for(var i = 0; i < jr.length; i++){   
        $('#reviews_body').append('<tr><td>' + obj[i].username + '</td><td>' + obj[i].review + '</td><td>' + obj[i].review_score + '</td></tr>');
    }
    
}

function booking_change_status(booking_id, new_status){
    console.log("new_status: " + new_status);
    let data = {};
    data['booking_id'] = booking_id;
    data['new_status'] = new_status;
    let jsonData = JSON.stringify(data);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'UpdateBooking');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if (xhr.readyState === 4 && xhr.status === 200){
            bookings_init();
        } else if (xhr.status !== 200){
            alert(xhr.status);
        }
    };
}

function avail_keep_init(){
    
    $('#login_msg').empty();
    $('#book_area').empty();
    $('#review_area').empty();
    $('#login_wrapper').load('available_keepers.html');
    
    let data = {};
    data['username'] = logged_in_username;
    let jsonData = JSON.stringify(data);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'AvailableKeepers');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if (xhr.readyState === 4 && xhr.status === 200){
            show_available_keepers(xhr.responseText);
        } else if (xhr.status !== 200){
            alert(xhr.status);
        }
    };
}

function show_available_keepers(response){
    
    const jr = JSON.parse(response);
    
    let text = '[{';
    for(var i = 0; i < jr.length; i++){
        text = text + jr[i];
        if(i < jr.length - 1)
            text = text + '},{';
    }
    text = text + '}]';
    const obj = JSON.parse(text);
    
    for(var i = 0; i < jr.length; i++){
        let daily_price = obj[i].price;
        daily_price = daily_price.slice(5);
        daily_price = daily_price.slice(0, -1);
        
        let uname = obj[i].username;
        
        $('#table_body').append('<tr><td>' + obj[i].username + '</td><td>' + obj[i].address + '</td><td>' + obj[i].price + '</td><td><button type="button" onclick="book_keeper(\'' + uname + '\',' + daily_price + ')">Book</button></td></tr>');
    }

}

function book_keeper(username, price){
    
    keeper_uname = username;
    keeper_price = price;
    
    $('#book_area').load("booking_form.html");
    
}

function show_book_msg(opCode){
    
    if(opCode === "who"){
        $('#who').append("Booking " + keeper_uname);
    }else if(opCode === "book_s"){
        $('#book_msg').append("Booking was successful!");
    }else if(opCode === "book_n"){
        $('#book_msg').append("Booking was unsuccessful! You already have a booking");
    }
}

function show_final_price(){
    var local_price = keeper_price;
    
    var from_date = new Date($('#fromdate').val());
    var from_day = from_date.getDate();
    var from_month = from_date.getMonth() + 1;
    var from_year = from_date.getFullYear();
    
    var to_date = new Date($('#todate').val());
    var to_day = to_date.getDate();
    var to_month = to_date.getMonth() + 1;
    var to_year = to_date.getFullYear();
    
    var days = 0;
    
    if(from_year !== to_year)
        days += 365 * (to_year - from_year);
    
    if(from_month !== to_month)
        days += 30 * (to_month - from_month);
    
    if(from_day !== to_day)
        days += to_day - from_day;
    
    local_price = local_price * days;
    glob_final_price = local_price;
    
    $('#final_price').empty();
    $('#final_price').append("Final Price: " + local_price + "€");
}

function bookPOST(){
    let myForm = document.getElementById("book_form");
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => data[key] = value);
    
    data['o_username'] = logged_in_username;
    data['k_username'] = keeper_uname;
    data['status'] = "requested";
    data['price'] = glob_final_price;
    
    let jsonData = JSON.stringify(data);
    
    xhr.open('POST', 'BookRegister');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            show_book_msg('book_s');
        }else if(xhr.status !== 200){
            show_book_msg('book_n');
        }
    };
}

function admin_delete(id, userType){
    let data = {};
    data['id'] = id + '';
    data['userType'] = userType;
    let jsonData = JSON.stringify(data);
    console.log("admin_delete says: " + jsonData);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'AdminDelete');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if (xhr.readyState === 4 && xhr.status === 200){
            console.log("Status 200");
            $('#login_wrapper').load('jsp/admin_page.jsp');
        } else if (xhr.status !== 200){
            alert(xhr.status);
        }
    };
}

function logout(){
    logged_in_username = "";
    logged_in_password = "";
    logged_in_userType = "";
    logged_in_id = "";
}

function updateInit(){
    
    $('#login_msg').empty();
    $('#book_area').empty();
    $('#review_area').empty();
    $('#login_wrapper').load('profile.html');
    
    let data = {};
    data['username'] = logged_in_username;
    data['password'] = logged_in_password;
    data['userType'] = logged_in_userType;
    let jsonData = JSON.stringify(data);
    //console.log("updateInit says: " + jsonData);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'UpdateGetInfo');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if (xhr.readyState === 4 && xhr.status === 200){
            updateView(xhr.responseText);
        } else if (xhr.status !== 200){
            alert(xhr.status);
        }
    };
}

function updateView(response){
    
    //console.log("updateView says: response = " + response);
    
    if(logged_in_userType === "owner")
        $('#hiddenForm').hide();
    else if(logged_in_userType === "keeper")
        $('#hiddenForm').show();  
    
    const jr = JSON.parse(response);
    
    $('#username').val(jr.username);
    $('#email').val(jr.email);
    $('#pwd').val(jr.password);
    $('#cnfmpwd').val(jr.password);
    $('#firstname').val(jr.firstname);
    $('#lastname').val(jr.lastname);
    $('#birthdate').val(jr.birthdate);
    if(jr.gender.toString() === "Male"){
        $('#male').prop("checked", true);
        $('#fmale').prop("checked", false);
    }else{
        $('#fmale').prop("checked", true);
        $('#male').prop("checked", false);
    }
    if(logged_in_userType === "keeper"){
        console.log("eimai edw");
        if(jr.property === "Indoor"){
            $('#inside').prop("checked", true);
            $('#outside').prop("checked", false);
            $('#both').prop("checked", false);
        }else if(jr.property === "Outdoor"){
            $('#outside').prop("checked", true);
            $('#inside').prop("checked", false);
            $('#both').prop("checked", false);
        }else if(jr.property === "Both"){
            $('#both').prop("checked", true);
            $('#inside').prop("checked", false);
            $('#outside').prop("checked", false);
        }
        
        if(jr.catkeeper === "true" && jr.dogkeeper === "false"){
            $('#cat').prop("checked", true);
            $('#dog').prop("checked", false);
            $('#bothAn').prop("checked", false);
        }else if(jr.catkeeper === "false" && jr.dogkeeper === "true"){
            $('#cat').prop("checked", false);
            $('#dog').prop("checked", true);
            $('#bothAn').prop("checked", false);
        }else if(jr.catkeeper === "true" && jr.dogkeeper === "true"){
            $('#cat').prop("checked", false);
            $('#dog').prop("checked", false);
            $('#bothAn').prop("checked", true);
        }
        
        $('#catp').val(jr.catprice);
        $('#dogp').val(jr.dogprice);
        $('#propdesc').val(jr.propertydescription);
    }
    $('#country').val(jr.country);
    $('#city').val(jr.city);
    $('#address').val(jr.address);
    $('#personalpage').val(jr.personalpage);
    $('#job').val(jr.job);
    $('#telephone').val(jr.telephone);
    
    if(logged_in_userType === "keeper")
        $('#book_area').append('<table><thead><tr><th>Number of Accommodations</th><th>Total Days</th></tr></thead><tbody><tr><td>' + jr.num_of_acc + '</td><td>' + jr.total_days + '</td></tr></tbody></table>');
    
}

// TODO if userType is keeper then put the correct data
//      for the fields: property, catkeeper, dogkeeper, catprice, dogprice
function updatePOST(){
    let myForm = document.getElementById("update_form");
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => data[key] = value);
    data['userType'] = logged_in_userType;
    data['lat'] = "0";
    data['lon'] = "0";
    
    if(data['animal'] === "cat"){
        data['catkeeper'] = "true";
        data['dogkeeper'] = "false";
        data['dogprice'] = "0";
    }else if(data['animal'] === "dog"){
        data['catkeeper'] = "false";
        data['dogkeeper'] = "true";
        data['catprice'] = "0";
    }else if(data['animal'] === "both"){
        data['catkeeper'] = "true";
        data['dogkeeper'] = "true";
    }
    
    let jsonData = JSON.stringify(data);
    
    console.log("updatePOST says: " + jsonData);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'Update');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            updateInit();
        }else if(xhr.status !== 200){
            alert(xhr.status);
        }
    };
}

function append_pet_buttons(){
    $('#login_msg').empty();
    $('#book_area').empty();
    $('#review_area').empty();
    $('#login_wrapper').empty();
    $('#login_wrapper').append('<button id="reg_pet" class="buttons" type="button" onclick="register_pet_init()">Register Pet</button>');
    $('#login_wrapper').append('<button id="up_pet" class="buttons" type="button" onclick="update_pet_init()">Update Pet</button>');
}

var opCode = "";

function choose_opCode(){
    if(opCode === "register")
        register_pet_POST();
    else if(opCode === "update")
        update_pet_POST();
    else
        console.log("opCode not recognized: " + opCode);
}

function register_pet_init(){
    opCode = "register";
    can_register_pet = false;
    $('#login_msg').empty();
    $('#login_wrapper').load("pet_reg_up.html");
}

function register_pet_POST(){
    let myForm = document.getElementById("pet_form");
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => data[key] = value);
    data['username'] = logged_in_username;
    let jsonData = JSON.stringify(data);
    
    console.log(jsonData);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'PetRegister');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            console.log("Pet Registered Successfully");
            $('#pet_form').hide();
            $('#ajax_msg').append("Pet Registered Successfully!");
        }else if(xhr.status !== 200){
            alert(xhr.status);
        }
    };
}

function update_pet_init(){
    opCode = "update";
    $('#login_msg').empty();
    $('#login_wrapper').load("pet_reg_up.html");
    
    const data = {};
    data['username'] = logged_in_username;
    let jsonData = JSON.stringify(data);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'GetPetInfo');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            update_view_pet(xhr.responseText);
        }else if(xhr.status !== 200){
            alert(xhr.status);
        }
    };
}

function update_view_pet(response){
    const jr = JSON.parse(response);
    
    if(jr.type === "cat"){
        $('#cat').prop("checked", true);
        $('#dog').prop("checked", false);
    }else if(jr.type === "dog"){
        $('#cat').prop("checked", false);
        $('#dog').prop("checked", true);
    }
    
    if(jr.gender === "male"){
        $('#male').prop("checked", true);
        $('#fmale').prop("checked", false);
    }else if(jr.gender === "female"){
        $('#male').prop("checked", false);
        $('#fmale').prop("checked", true);
    }
    
    $('#pet_id').val(jr.pet_id);
    $('#pet_id').prop("readonly", true);
    $('#name').val(jr.name);
    $('#breed').val(jr.breed);
    $('#birthyear').val(jr.birthyear);
    $('#weight').val(jr.weight);
    $('#desc').val(jr.description);
    $('#photo').val(jr.photo);
}

function update_pet_POST(){
    let myForm = document.getElementById("pet_form");
    let formData = new FormData(myForm);
    const data = {};
    formData.forEach((value, key) => data[key] = value);
    data['username'] = logged_in_username;
    let jsonData = JSON.stringify(data);
    
    console.log("updatePOST says: " + jsonData);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'PetUpdate');
    xhr.send(jsonData);
    
    xhr.onload = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            update_pet_init();
        }else if(xhr.status !== 200){
            alert(xhr.status);
        }
    };
}