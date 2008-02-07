/*
    author: Hannes Derler
    date: 02.01.2008
*/

	function CalendarJS() {
		this.now = new Date();
		this.dayname = ["Mo","Di","Mi","Do","Fr","Sa","So"];
		this.monthname = ["Januar","Februar","Maerz","April","Mai","Juni","Juli","August","September","Oktober","November","Dezember"];
		this.dayspermonth = [31,28,31,30,31,30,31,31,30,31,30,31];
		this.tooltip = ["vorheriger Monat","naechster Monat"];
		this.monthCell = document.createElement("th");
		this.tableHead = null;
		this.parEl = null;
		
		this.init = function( id ) {
			this.date = this.now.getDate();
			this.month = this.mm = this.now.getMonth();
			this.year = this.yy = this.now.getFullYear();
			this.monthCell.colSpan = 5;
			this.monthCell.appendChild(document.createTextNode( this.monthname[this.mm]+" "+this.yy ));
			this.tableHead = this.createTableHead();
			this.parEl = document.getElementById( id );
			this.show();
		}
		
		this.removeElements = function( Obj ) {
			for (var i=0; i<Obj.childNodes.length; i++)
				Obj.removeChild(Obj.childNodes[i]);
			return Obj;
		}
			
		this.show = function() {
			this.parEl = this.removeElements( this.parEl );
			this.monthCell.firstChild.replaceData(0, this.monthCell.firstChild.nodeValue.length, this.monthname[this.mm]+" "+this.yy);
			var table = document.createElement("table");
			table.appendChild( this.createTableBody() );
			table.appendChild( this.tableHead );
			this.parEl.appendChild( table );
		}
				
		this.createTableHead = function() {
			var thead = document.createElement("thead");
			var tr = document.createElement("tr");
			var th = document.createElement("th");
			th.appendChild(document.createTextNode( "\u00AB" ));
			th.Instanz = this;
			th.onclick = function() { this.Instanz.switchMonth("prev"); };
			th.title = this.tooltip[0];
			try { th.style.cursor = "pointer"; } catch(e){ th.style.cursor = "hand"; }
			tr.appendChild( th );
			tr.appendChild( this.monthCell );			
			th = document.createElement("th");
			th.appendChild(document.createTextNode( "\u00BB" ));
			th.Instanz = this;
			th.onclick = function() { this.Instanz.switchMonth("next"); };
			th.title = this.tooltip[1];
			try { th.style.cursor = "pointer"; } catch(e){ th.style.cursor = "hand"; }
			tr.appendChild( th );
			thead.appendChild( tr );
			tr = document.createElement('tr');
			for (var i=0; i<this.dayname.length; i++)
				tr.appendChild( this.getCell("th", this.dayname[i], "weekday" ) );
			thead.appendChild( tr );
			return thead;
		}
		
		this.createTableBody = function() {
			var sevendaysaweek = 0;
			var begin = new Date(this.yy, this.mm, 1);
			var firstday = begin.getDay()-1;
			if (firstday < 0)
				firstday = 6;
			if ((this.yy%4==0) && ((this.yy%100!=0) || (this.yy%400==0)))
				this.dayspermonth[1] = 29;
			
			var tbody = document.createElement("tbody");
			var tr = document.createElement('tr');
			
			for (var i=0; i<firstday; i++, sevendaysaweek++)
				tr.appendChild( this.getCell( "td", " "," ", " ", null ) );

			for (var i=1; i<=this.dayspermonth[this.mm]; i++, sevendaysaweek++){
				if (this.dayname.length == sevendaysaweek){
					tbody.appendChild( tr );
					tr = document.createElement('tr');
					sevendaysaweek = 0;
				}
				if (i==this.date && this.mm==this.month && this.yy==this.year && (sevendaysaweek == 5 || sevendaysaweek == 6))
					tr.appendChild( this.getCell( "td", i, this.mm, this.yy, "today weekend" ) );
				else if (i==this.date && this.mm==this.month && this.yy==this.year)
					tr.appendChild( this.getCell( "td", i, this.mm, this.yy, "today" ) );
				else if (sevendaysaweek == 5 || sevendaysaweek == 6)
					tr.appendChild( this.getCell( "td", i, this.mm, this.yy, "weekend" ) );
				else
					tr.appendChild( this.getCell( "td", i, this.mm, this.yy, null ) );
			
				if(!document.form.endDay.value){
					document.form.endDay.value=this.date;
					document.form.endMonth.value=(this.mm+1);
					document.form.endYear.value=this.now.getFullYear();
					document.form.startDay.value=this.date;
					document.form.startMonth.value=(this.mm+1);
					document.form.startYear.value=this.now.getFullYear();	    
					document.form.selDateView.value= this.date+"."+(this.mm+1)+"."+this.now.getFullYear();
				}
			}
	
			for (var i=sevendaysaweek; i<this.dayname.length; i++)
				tr.appendChild( this.getCell( "td", " "," ", " ", null  ) );
			tbody.appendChild( tr );
			return tbody;
		}
		
		this.getCell = function(tag, str, mom, yey, cssClass) {
			var El = document.createElement( tag );
            mom+=1;

            if(str!="Mo" || str!="Di" || str!="Mi" || str!="Do" || str!="Fr" || str!="Sa" || str!="So" ){
		            El.onclick = function() { 
		                //Startdatum
		                document.form.startDay.value=str;
					    document.form.startMonth.value=mom;
					    document.form.startYear.value=yey;
					    
					    //Anzeige
					    document.form.selDateView.value= str+"."+mom+"."+yey;
					    
					    //Enddatum
					    document.form.endDay.value=str;
					    document.form.endMonth.value=mom;
					    document.form.endYear.value=yey;
					    
					    modityDateIfNeeded();
		             };
	             
            }
			El.appendChild(document.createTextNode( str ));
			if (cssClass != null)
				El.className = cssClass;
			return El;
		}
		
		this.switchMonth = function( s ){
			switch (s) {
				case "prev": 
					this.yy = (this.mm == 0)?this.yy-1:this.yy;
					this.mm = (this.mm == 0)?11:this.mm-1;
				break;
				
				case "next":
					this.yy = (this.mm == 11)?this.yy+1:this.yy;
					this.mm = (this.mm == 11)?0:this.mm+1;
				break;
			}
			this.show();
		}
	}
	
	var DOMContentLoaded = false;
	function addContentLoadListener (func) {
		if (document.addEventListener) {
			var DOMContentLoadFunction = function () {
				window.DOMContentLoaded = true;
				func();
			};
			document.addEventListener("DOMContentLoaded", DOMContentLoadFunction, false);
		}
		var oldfunc = (window.onload || new Function());
		window.onload = function () {
			if (!window.DOMContentLoaded) {
				oldfunc();
				func();
			}
		};
	}
	
	addContentLoadListener( function() { 
			new CalendarJS().init("calendar");
	} );