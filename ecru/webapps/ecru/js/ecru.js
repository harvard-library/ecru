/*
 *  ecru.js contains functions for processing a search.
 *  
 *  It assumes jQuery is included.
 *  
 *  29 August 2013 Bobbi Fox
 *  
*/
// Schools
var school_abbr = new Array();
school_abbr['GSE'] = 'Graduate School of Education';                                                                         
school_abbr['HSPH'] = 'School of Public Health';
school_abbr['HDS'] = 'Divinity School';        
school_abbr['RAD'] = 'Radcliffe';              
school_abbr['HU'] = 'Harvard';                
school_abbr['FAS'] = 'Faculty of Arts and Sciences';
school_abbr['GSD'] = 'Graduate School of Design';
school_abbr['HLS'] = 'Law School';             
school_abbr['ARB'] = 'Arnold Arboretum';       
school_abbr['SUM'] = ' Summer School';          
school_abbr['EXT'] = 'Extension School';       
school_abbr['BCIS'] = 'Berkman Center';         
school_abbr['DCE'] = 'Division of Continuing Education';
school_abbr['KSG'] = 'Kennedy School';         
school_abbr['HILR'] = 'HILR';                   
school_abbr['HKS'] = 'Harvard Kennedy School (HKS)';
school_abbr['HCL'] = ' Harvard College Library';
school_abbr['HMS'] = 'Harvard Medical School'; 
school_abbr['HSDM'] = 'Harvard School of Dental Medicine';
school_abbr['HBSDOC'] = 'Harvard Business School Doctoral Program';
school_abbr['HBSMBA'] = 'Harvard Business School MBA Program';


//Libraries
var lib_abbr = new Array();

lib_abbr['DIV']='Andover-Harvard';
lib_abbr['BIO']='Biological Labs';      
lib_abbr['BOT']='Botany';               
lib_abbr['CAB']='Cabot Science';        
lib_abbr['CHE']='Chemistry';            
lib_abbr['MED']='Countway Medicine';    
lib_abbr['FAL']='Fine Arts';            
lib_abbr['GUT']='Gutman Education';     
lib_abbr['HYL']='Harvard- Yenching';    
lib_abbr['HIL']='Hilles';               
lib_abbr['KSG']='Kennedy Sch of Gov';   
lib_abbr['GEO']='Kummel Geological Sci';
lib_abbr['LAM']='Lamont';               
lib_abbr['LAW']='Law School';           
lib_abbr['LIT']='Littauer';             
lib_abbr['DES']='Loeb Design';          
lib_abbr['MUS']='Loeb Music';           
lib_abbr['MCK']='Mckay Applied Sci';    
lib_abbr['MCZ']='Museum Comparative Zoology';  
lib_abbr['PHY']='Physics';              
lib_abbr['RUB']='Rubel (Fine Arts)';    
lib_abbr['TOZ']='Tozzer';               
lib_abbr['WID']='Widener';              
lib_abbr['WOL']='Wolbach';              
lib_abbr['CGI']='Ctr. for Gov. Intl. Stud. (CGIS)';                    
lib_abbr['GRO']='Grossman (Extension)'; 
lib_abbr['FUN']='Fung Library';  
// get the "previous" and "next", nunber found
function getSummary(summary) {
	var sum = "<div class='summary'><h3>";
	var to = summary.start + summary.rows;
	if (summary.numFound == 0) {
		sum = sum + "No results Found</h3></div>";
		return $(sum);
	}
	if (to > summary.numFound) {
		to = summary.numFound;
	}
	
	sum = sum + (summary.start + 1) + " -- " + to + " of " +  summary.numFound + " found</h3></div>";
	var $sum = $(sum);
	var $pn = $("<div class='pn'></div>");
	var $p = $("<span class='prev'></span>");
	var $n = $("<span class='next'></span>");
	if (summary.prevUrl) {
		$p.append("<a class='solr' href='" + summary.prevUrl + "'> << Previous</a>");
	}
	if (summary.nextUrl) {
		$n.append("<a class='solr' href='" + summary.nextUrl +  "'> Next >></a>");
	}
	$pn.append($p).append($n);
	$sum.append($pn)
	return $sum;
}
function doFaceting(faceting) {
	if (faceting.listFacets.length == 0) {
		return "";
	}
	var str = "<h2>Refine Your Results</h2>  <ul>";
		 for (var i=0; i< faceting.listFacets.length; i++) {
			 var list = faceting.listFacets[i];
			 var header = list.header;
			 if (header === "Location") {
				 header = "Readings by Location";
			 }
			 else if (header === "School") {
				 header = "Courses by School";
			 }
			 str = str + "<li class='f_list'><div><h3>" + header + "</h3><ul>\n" ;
			 for (var j = 0; j < list.facets.length; j++) {
				 var fac = list.facets[j];
				 var title = fac.title;
				 if (list.header === "Location") {
					 title = lib_abbr[title];
				 }
				 else if (list.header === "School") {
					 title = school_abbr[title];
				 }
				 
				 str = str + "<li class='f'><a class='solr' href='" + fac.url + "'>";
				 str = str + title + "</a> (" + fac.count + ")</li>"
			 }
			 str = str + "</ul></div></li>";
		 }
	str = str + "</ul>";
	return str;
	
	
}


// handle and item, returning a jQuery object (<li>)
function doItem(item) {
	var liClass=item.type;
	var $li = $("<li class='" + item.type + "' id='" + item.id + "'></li>");
	
    var thisResult = item.index + ". <span class='title'>" + item.displayLabel + "</span>";
    if (item.type == "reading") {
  	  thisResult = thisResult + doReading(item);
    }
    else {
  	  thisResult = thisResult + doCourse(item);
    }
    
    $li.append(thisResult);
    return $li;

}

function doReading(item) {
	var str = " (" + item.term + ")";
	str = str + "<br/> Reading Status: <em class='status'>" + item.status + "</em>"; 
	
	if (item.doiId && item.doiId != "DOI") {
		str =str + " <div class='doi'>DOI : " + item.doiId +"</div>";
	}
	if (item.pubmed) {
		str =str + " <div class='pubmed'>PUBMED: " + item.pubmed +"</div>";;
	}
	if (item.isbn) {
		str = str + " <div class='isbn'>ISBN: " + item.isbn + "</div>";
	}
	if (item.issn) {
		str = str + " <div class='issn'>ISSN: " + item.issn  + "</div>";
	}
	if (item.digUrl) {
		str =str + "<br/> <a href='" + item.digUrl + "' class='online' target='digRead'><img src='icons/online.png' title='Read Online'/></a>";
	}
	if (item.system) {
		str += "<br/>"
		if (item.system === "HVD01") {
			str =str + " <a href='http://discovery.lib.harvard.edu/?itemid=|library/m/aleph|" + 
			item.sysId + "' target='hollis'>Hollis Record</a>"; 
		}
		else if (item.system === "HVD30") {
			str =str + " <a href='" +
					"http://lms01.harvard.edu/F/?func=item-global&doc_library=HVD30&doc_number=" 
			+ item.sysId + "'";
			str = str + " target='hollis'>Hollis Availability</a>"
		}
	}
	
	str = str + "<br/> <a href='" + item.courseUrl +"' target='_new' class='solr'>Course Information</a>"
	str = str + "&nbsp;&nbsp;<a href='" + baseUrl + "readings/courses/" + item.courseId + "?sort=reading.first_author+asc";
	str = str +"' target='_new' class='solr'>All readings for the course </a> ";
		

	return str;
}
function doCourse(item) {
	var str = " [" + item.division + "-" + item.catNo +"] (" + item.term + ")";
	if (item.readings) {
		str = str + "<br/> <a href='" + item.readingsUrl+"' target='_new' class='solr'>Readings</a>";
	}
	else {
		str = str + "<br/><span class='no'>No readings available at this time</span>";
	}
	if (item.url) {
		str = str + "<br/><a href='" + item.url + " 'target='isite'>Course iSite</a>";
	}
	else {
		str = str + "<br/><span class='no'>No Course iSite  available at this time</span>";
	}
	return str;
}