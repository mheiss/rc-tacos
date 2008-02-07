/*
    author: Hannes Derler
    date: 07.02.2008
*/
function modityDateIfNeeded(){
    var dayspermonth = [31,28,31,30,31,30,31,31,30,31,30,31];
    var newEndDat = "";
    
    if(getRadioCheckedValue() == "night"){
        //Check and Set Day
        if(document.form.startHour.selectedIndex >= document.form.endHour.selectedIndex){
            currentEndDat = parseInt(document.form.endDay.value);
            newEndDat = currentEndDat + 1;
            document.form.endDay.value = newEndDat;
            
            //Check and Set Month
            if(document.form.endDay.value > dayspermonth[parseInt(document.form.startMonth.value)-1]){
                document.form.endDay.value = 1;
                currentEndDat = parseInt(document.form.endMonth.value);
                newEndDat = currentEndDat + 1;
                document.form.endMonth.value = newEndDat;
                
                //Check and Set Year
                if(parseInt(document.form.endMonth.value)>12){
                    document.form.endDay.value = 1;
                    document.form.endMonth.value = 1;
                    currentEndDat = parseInt(document.form.endYear.value);
                    newEndDat = currentEndDat + 1;
                    document.form.endYear.value = newEndDat;
                }
                
            }
        }
    }
    if(getRadioCheckedValue() == "day"){
        //Start = End
	    document.form.endDay.value = document.form.startDay.value;
	    document.form.endMonth.value = document.form.startMonth.value;
	    document.form.endYear.value = document.form.startYear.value;    
    }

}

function getRadioCheckedValue()
{
for (var i=0; i < document.form.rosterKind.length; i++)
   {
   if (document.form.rosterKind[i].checked)
      {
        return document.form.rosterKind[i].value;
      }
   }
}