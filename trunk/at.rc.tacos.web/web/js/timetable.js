    // Detect if the browser is IE or not.
    // If it is not IE, we assume that the browser is NS.
    var IE = document.all?true:false
    
    // If NS -- that is, !IE -- then set up for mouse capture
    if (!IE) document.captureEvents(Event.MOUSEMOVE)
    
    // Set-up to use getMouseXY function onMouseMove
    document.onmousemove = getMouseXY;
    
    // Temporary variables to hold mouse x-y pos.s
    var tempX = 0
    var tempY = 0
    
    // Main function to retrieve mouse x-y pos.s
    
    
function setup(){
    //alert(screen.availHeight + " != " + screen.height + "<br>" + screen.availWidth + " != " + screen.width);
    //var timeTabHeight = document.getElementById("TimeTab").getAttribute;
    //alert(timeTabHeight);
    //document.getElementById("TimeTab").style.height;
    //alert(document.getElementById("TimeTab").style.height);
    //document.getElementById("MainDivDay").style.height = screen.availHeight/2;
    //document.getElementById("MainDivDay").style.width = screen.availWidth/7;

    
}

function showInfo(){
  document.getElementById("InfoPanel").style.left = tempX
  document.getElementById("InfoPanel").style.top = tempY
}

function getMouseXY(e) {

  if (IE) { // grab the x-y pos.s if browser is IE
    tempX = event.clientX + document.body.scrollLeft
    tempY = event.clientY + document.body.scrollTop
  } else {  // grab the x-y pos.s if browser is NS
    tempX = e.pageX
    tempY = e.pageY
  }  
  // catch possible negative values in NS4
  if (tempX < 0){tempX = 0}
  if (tempY < 0){tempY = 0}  
  // show the position values in the form named Show
  // in the text fields named MouseX and MouseY
  //document.Show.MouseX.value = tempX
  //document.Show.MouseY.value = tempY

  
  return true
  
}

