/*
    author: Hannes Derler
    date: 17.12.2007
*/
function openPopupToDelete( address ) {
  popup = window.open(address, "Tacos Eintrag loeschen", "width=300,height=400,left=100,top=200");
  popup.focus();
    
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

function changeTime(sel){
    if(sel=="day"){
        document.form.startHour.selectedIndex = 5;
        document.form.endHour.selectedIndex = 17;
    }else if(sel=="night"){
        document.form.startHour.selectedIndex = 17;
        document.form.endHour.selectedIndex = 5;       
    }
}

function getWindowWidth(){
    var width = window.document.body.offsetWidth;
        if (document.getElementById) {
		   document.getElementById("test").style.left = width/2;
		} else if (document.all) {
		    document.all.test.style.left = width/2;
		}
    
}