$(document).ready(function(){
	
	$('.directLink').click(function(e){
		
		e.preventDefault();
			
		document.directLink.openDirectLink($(this).attr('href'));
		
	});
	
	
	if(typeof(window.ActiveXObject)!="undefined"){
		
		//use activeX to open file
		
		
	}else if(deployJava.getJREs()) {
		
		
		
		
	}else{
		//use NPAPI plugin
		
	}
	
});