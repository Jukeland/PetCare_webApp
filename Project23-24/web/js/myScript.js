var password = "";
var confirmPassword = "";
var pwdStrength = "";

var obj;
var longitude = 0;
var latitude = 0;

const uppercaseLetters = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
const hasLowerCaseLetters = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"];
const specialCharacters = ["!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "+", ",", ";", "?", "/", "[", "]", "{", "}", "<", ">", ".", "`"];


// this function checks if the two passwords match or not
// and lets the user know
function checkPassword(){
    password = document.getElementById("pwd").value;
    confirmPassword = document.getElementById("cnfmpwd").value;
    if(!(password === confirmPassword))
        document.getElementById("pwdWarning").innerHTML = "Password mismatch. Please input the same password";
    else
        document.getElementById("pwdWarning").innerHTML = "";
    checkPasswordStrength();
}


// this function checks the password strength 
// and lets the user know
function checkPasswordStrength(){
    var numbers = 0;
    var hasLowerCase = false;
    var hasUpperCase = false;
    var hasSpecialChar = false;
    const pwdLength = password.length;

    for(var i = 0; i < pwdLength; i++){
        for(var j = 0; j < uppercaseLetters.length; j++){
            if(password.charAt(i) == uppercaseLetters[j])
                hasUpperCase = true;
            else if(password.charAt(i) == hasLowerCaseLetters[j])
                hasLowerCase = true;
            else if(password.charAt(i) == specialCharacters[j])
                hasSpecialChar = true;
        }

        for(var j = 0; j < 10; j++){
            if(password.charAt(i) == j){
                numbers++;
                j = 11;
            }
        }
    }

    if(password === "cat" || password === "dog" || password === "gata" || password === "skulos"){
        document.getElementById("pwdStrength").style.color = "red";
        pwdStrength = "formidable";
    }else if(numbers/pwdLength >= 0.5){
        document.getElementById("pwdStrength").style.color = "red";
        pwdStrength = "weak";
    }else if(numbers > 0 && hasLowerCase && hasUpperCase && hasSpecialChar){
        document.getElementById("pwdStrength").style.color = "green";
        pwdStrength = "strong";
    }else{
        document.getElementById("pwdStrength").style.color = "orange";
        pwdStrength = "medium";
    }

    if(password !== "")
        document.getElementById("pwdStrength").innerHTML = "Password is " + pwdStrength;
    else
        document.getElementById("pwdStrength").innerHTML = "";
}


// this function hides and shows the user's password
function changeVisibility(opCode){
    console.log("opCode: " + opCode);
    if(document.getElementById("pwd").type === "password"){
        document.getElementById("pwd").type = "text";
        document.getElementById("cnfmpwd").type = "text";
        document.getElementById("hideShow").value = "Hide Password";
        document.getElementById("hideShow").innerHTML = "Hide Password";
    }else{
        document.getElementById("pwd").type = "password";
        document.getElementById("cnfmpwd").type = "password";
        document.getElementById("hideShow").value = "Show Password";
        document.getElementById("hideShow").innerHTML = "Show Password";
    }
}


// this function hides and shows the new elements of 
//the form if the user a pet keeper
function ifKeeper(){
    if(document.getElementById("keeper").checked){
        document.getElementById("hiddenForm").hidden = false;
    }else{
        document.getElementById("hiddenForm").hidden = true;
    }
    document.getElementById("inside").checked = false;
    document.getElementById("outside").checked = false;
    document.getElementById("both").checked = false;
    document.getElementById("cat").checked = false;
    document.getElementById("dog").checked = false;
    document.getElementById("bothAn").checked = false;
    document.getElementById("dogp").value = "";
    document.getElementById("catp").value = "";
    document.getElementById("propdesc").value = "";
}


// this function lets the user choose the pet price
// accordingly to what they chose as a pet
function animalAcc(){
    if(document.getElementById("cat").checked){
        document.getElementById("catPrice").hidden = false;
        document.getElementById("dogPrice").hidden = true;
    }else if(document.getElementById("dog").checked){
        document.getElementById("dogPrice").hidden = false;
        document.getElementById("catPrice").hidden = true;
    }else if(document.getElementById("bothAn").checked){
        document.getElementById("catPrice").hidden = false;
        document.getElementById("dogPrice").hidden = false;
    }
        
}


// this function lets the user choose only dog as a pet
// if he chose only outside as the pet accommodation
function isOutside(){
    if(document.getElementById("outside").checked){
        document.getElementById("dogAcc").hidden = false;
        document.getElementById("catAcc").hidden = true;
        document.getElementById("bothAcc").hidden = true;
    }else{
        document.getElementById("dogAcc").hidden = false;
        document.getElementById("catAcc").hidden = false;
        document.getElementById("bothAcc").hidden = false;
    }
}

function termsChecked(){
    if(document.getElementById("terms").checked)
        return true;
    else
        return false;
}

function printForm(){
    let myForm = document.getElementById("Form");
    let formData = new FormData(myForm);

    const data = {};
    
    formData.forEach((value, key) => {
        data[key] = value;
    });
    
    console.log(data);
}

function canSubmit(){
    if(pwdStrength === "weak" || pwdStrength === "formidable" || !termsChecked()){
        return false;
    }else{
        return true;
    }
}



var debug;

function printDebug(){
    console.log(debug);
}


function register(e){
    if(canSubmit()){
        registerPOST();
        return false;
    }
    $('#ajax_content').html("Logic Registration");
    e.preventDefault();
    console.log("cannot submit");
}

function registerPOST(){
    let myForm = document.getElementById("Form");
    let formData = new FormData(myForm);

    const data = {};
    formData.forEach((value, key) => {
        if(value === ""){
            value = "0";
        }
        data[key] = value;
    });

    data['lat'] = latitude.toString();
    data['lon'] = longitude.toString();
    
    var xhr = new XMLHttpRequest();
    
    xhr.onload = function(){
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            $('#ajax_content').html("Successful Registration. Now please log in!<br> Your Data");
            $('#ajax_content').append(createTableFromJSON(responseData));
            console.log("Engine");
        }else if (xhr.status !== 200) {
            $('#hope').append('Request failed. Returned status of ' + xhr.status + "<br>");
           const responseData = JSON.parse(xhr.responseText);
            for (const x in responseData) {
                $('#hope').append("<p style='color:red'>" + x + "=" + responseData[x] + "</p>");
            }
        }
    };
    
    xhr.open('POST', 'Register');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(data));
}

function loadDoc(){
    const data = null;
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener('readystatechange', function () {
	    if (this.readyState === this.DONE){
            obj = JSON.parse(xhr.responseText);
            if(checkLocation())
                setUpMap();
            else{
                document.getElementById("Map").hidden = true;
                document.getElementById("showMap").hidden = true;
            }
                
            
	    }
    });

    var addressName = document.getElementById("address").value;
    var city = document.getElementById("city").value;
    var country = document.getElementById("country").value;
    var address = addressName + " " + city + " " + country;

    xhr.open('GET', "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q=" + address + "&accept-language=en&polygon_threshold=0.0");
    xhr.setRequestHeader('X-RapidAPI-Key', '73c902ed6dmshcf0b92c050bc189p1894eajsn916f46cced2a');
    xhr.setRequestHeader('X-RapidAPI-Host', 'forward-reverse-geocoding.p.rapidapi.com');

    xhr.send(data);
}

function checkLocation(){
    if(obj[0] === undefined){
        document.getElementById("addressWarning").style.color = "red";
        document.getElementById("addressWarning").innerHTML = "Address or City Name not recognised";
        return false;
    }

    var text = obj[0].display_name;

    if(text.search("Heraklion") === -1){
        document.getElementById("addressWarning").style.color = "red";
        document.getElementById("addressWarning").innerHTML = "This service is available only in Heraklion";
        return false;
    }

    document.getElementById("addressWarning").style.color = "green";
    document.getElementById("addressWarning").innerHTML = "Address Recognised";
    longitude = obj[0].lon;
    latitude = obj[0].lat;
    return true;
}



function setPosition(lat, lon){
    var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
    var toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
    var position = new OpenLayers.LonLat(lon, lat).transform( fromProjection, toProjection);
    return position;
}

function handler(position, message){
    var popup = new OpenLayers.Popup.FramedCloud("Popup", 
        position, null,
        message, null,
        true 
    );
    map.addPopup(popup);
}

function setUpMap(){
    const mapDiv = document.getElementById("Map");
    mapDiv.innerHTML = "";
    const showMapButton = document.getElementById("showMap");
    if(mapDiv.hidden){
        mapDiv.hidden = false;
        showMapButton.hidden = false;
        showMapButton.value = "Hide Map";
    }else{
        mapDiv.hidden = true;
        showMapButton.hidden = true;
        showMapButton.value = "Show Map";
    }

    if(checkLocation()){
        showMapButton.hidden = false;
        map = new OpenLayers.Map("Map");
        var mapnik = new OpenLayers.Layer.OSM();
        map.addLayer(mapnik);

        var markers = new OpenLayers.Layer.Markers( "Markers" );
        map.addLayer(markers);

        var position=setPosition(latitude,longitude);
        var mar=new OpenLayers.Marker(position);
        markers.addMarker(mar);	
        mar.events.register('mousedown', mar, function(evt) { 
            handler(position,'Your Address');}
        );

        const zoom = 15;
        map.setCenter(position, zoom);
    }
}

function create_DB(){
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'InitDB');
    xhr.send();
    
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
              console.log("The database has been created successfully");
        } else if (xhr.status !== 200) {
             console.log("The database has already been created");
        }
    };
    
}