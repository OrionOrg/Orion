	/**
	* Optionally used to deploy multiple versions of the applet for mixed
	* environments.  Oracle uses document.write(), which puts the applet at the
	* top of the page, bumping all HTML content down.
	*/
	//deployQZ();
	
	/** root of app */
	var sCtx;  
	
	/**
	* Deploys different versions of the applet depending on Java version.
	* Useful for removing warning dialogs for Java 6.  This function is optional
	* however, if used, should replace the <applet> method.  Needed to address 
	* MANIFEST.MF TrustedLibrary=true discrepency between JRE6 and JRE7.
	*/
	function deployQZ(ctx) {
		var sCtx = ctx;
		var attributes = {id: "qz", code:'qz.PrintApplet.class', 
			archive:sCtx + '/qz-print/qz-print.jar', width:1, height:1};
		var parameters = {jnlp_href: sCtx + '/qz-print/qz-print_jnlp.jnlp', 
			cache_option:'plugin', disable_logging:'false', 
			initial_focus:'false'};
		if (deployJava.versionCheck("1.7+") == true) {}
		else if (deployJava.versionCheck("1.6+") == true) {
			attributes['archive'] = sCtx + '/qz-print/jre6/qz-print.jar';
			parameters['jnlp_href'] = sCtx + '/qz-print/jre6/qz-print_jnlp.jnlp';
		}
		deployJava.runApplet(attributes, parameters, '1.5');
	}
	
	/**
	* Automatically gets called when applet has loaded.
	*/
	function qzReady() {
		// Setup our global qz object
		window["qz"] = document.getElementById('qz');
		//var title = document.getElementById("title");
		if (qz) {
			try {
				//title.innerHTML = title.innerHTML + " " + qz.getVersion();
				//document.getElementById("content").style.background = "#F0F0F0";
			} catch(err) { // LiveConnect error, display a detailed meesage
				//document.getElementById("content").style.background = "#F5A9A9";
				alert("ERROR:  \nThe applet did not load correctly.  Communication to the " + 
					"applet has failed, likely caused by Java Security Settings.  \n\n" + 
					"CAUSE:  \nJava 7 update 25 and higher block LiveConnect calls " + 
					"once Oracle has marked that version as outdated, which " + 
					"is likely the cause.  \n\nSOLUTION:  \n  1. Update Java to the latest " + 
					"Java version \n          (or)\n  2. Lower the security " + 
					"settings from the Java Control Panel.");
		  }
	  }
	}
	
	/**
	* Returns whether or not the applet is not ready to print.
	* Displays an alert if not ready.
	*/
	function notReady() {
		// If applet is not loaded, display an error
		if (!isLoaded()) {
			return true;
		}
		// If a printer hasn't been selected, display a message.
		else if (!qz.getPrinter()) {
			alert('Please select a printer first by using the "Detect Printer" button.');
			return true;
		}
		return false;
	}
	
	/**
	* Returns is the applet is not loaded properly
	*/
	function isLoaded() {
		if (!qz) {
			alert('Error:\n\n\tPrint plugin is NOT loaded!');
			return false;
		} else {
			try {
				if (!qz.isActive()) {
					alert('Error:\n\n\tPrint plugin is loaded but NOT active!');
					return false;
				}
			} catch (err) {
				//alert('Error:\n\n\tPrint plugin is NOT loaded properly!');
				return false;
			}
		}
		return true;
	}
	
	/**
	* Automatically gets called when "qz.print()" is finished.
	*/
	function qzDonePrinting() {
		// Alert error, if any
		if (qz.getException()) {
			alert('Error printing:\n\n\t' + qz.getException().getLocalizedMessage());
			qz.clearException();
			print_success("0","打印机连接失败！");
			return; 
		}
		print_success("1","");
		// Alert success message
		//alert('Successfully sent print data to "' + qz.getPrinter() + '" queue.');
	}
	
	/***************************************************************************
	* Prototype function for finding the "default printer" on the system
	* Usage:
	*    qz.findPrinter();
	*    window['qzDoneFinding'] = function() { alert(qz.getPrinter()); };
	***************************************************************************/
	function useDefaultPrinter() {
		if (isLoaded()) {
			// Searches for default printer
			qz.findPrinter();
			
			// Automatically gets called when "qz.findPrinter()" is finished.
			window['qzDoneFinding'] = function() {
				// Alert the printer name to user
				var printer = qz.getPrinter();
				alert(printer !== null ? 'Default printer found: "' + printer + '"':
					'Default printer ' + 'not found');
				
				// Remove reference to this function
				window['qzDoneFinding'] = null;
			};
		}
	}
	
	/***************************************************************************
	* Prototype function for finding the closest match to a printer name.
	* Usage:
	*    qz.findPrinter('zebra');
	*    window['qzDoneFinding'] = function() { alert(qz.getPrinter()); };
	***************************************************************************/
	function findPrinter(name) {
		// Get printer name from input box
		var value="";
		if (name) {
			value = name;
		}
		 
		if (isLoaded()) {
			// Searches for locally installed printer with specified name
			qz.findPrinter(value);
		 
			// Automatically gets called when "qz.findPrinter()" is finished.
			window['qzDoneFinding'] = function() {
			 
				var printer = qz.getPrinter();
				
				// Alert the printer name to user
				//alert(printer !== null ? 'Printer found: "' + printer + 
				//	'" after searching for "' + p.value + '"' : 'Printer "' + 
				//	p.value + '" not found.');
				
				// Remove reference to this function
				window['qzDoneFinding'] = null;
			 
				return true;
			};
		}
		
		return false;
	}
	
	/***************************************************************************
	* Prototype function for listing all printers attached to the system
	* Usage:
	*    qz.findPrinter('\\{dummy_text\\}');
	*    window['qzDoneFinding'] = function() { alert(qz.getPrinters()); };
	***************************************************************************/
	function findPrinters() {
		if (isLoaded()) {
			// Searches for a locally installed printer with a bogus name
			qz.findPrinter('\\{bogus_printer\\}');
			
			// Automatically gets called when "qz.findPrinter()" is finished.
			window['qzDoneFinding'] = function() {
				// Get the CSV listing of attached printers
				var printers = qz.getPrinters().split(',');
				for (i in printers) {
					alert(printers[i] ? printers[i] : 'Unknown');      
				}
				
				// Remove reference to this function
				window['qzDoneFinding'] = null;
			};
		}
	}
	
	/***************************************************************************
	* Prototype function for printing a PDF to a PostScript capable printer.
	* Not to be used in combination with raw printers.
	* Usage:
	*    qz.appendPDF('/path/to/sample.pdf');
	*    window['qzDoneAppending'] = function() { qz.printPS(); };
	***************************************************************************/ 
	function printPDF(pdfPath,dyfs) {
		if (notReady()) { return; }
		// Append our pdf (only one pdf can be appended per print)
		//qz.allowMultipleInstances(false);
		qz.setCopies(dyfs);	
		qz.appendPDF(pdfPath);	 
		
	    // var sURL = "http://localhost:8080/Test/misc/pdf_sample.pdf";
	  
	    //qz.appendPDF(getPath() + "qz-print/misc/pdf_sample.pdf");	     
		//qz.appendPDF(sURL);
		
		// Automatically gets called when "qz.appendPDF()" is finished.
		window['qzDoneAppending'] = function(){
			// Tell the applet to print PostScript.
			// alert(dyfs);		
			 qz.printPS();
			 //qz.print();
			// Remove reference to this function
			window['qzDoneAppending'] = null;
		};
	}
	
	/***************************************************************************
	****************************************************************************
	* *                          HELPER FUNCTIONS                             **
	****************************************************************************
	***************************************************************************/
	
	
	/***************************************************************************
	* Gets the current url's path, such as http://site.com/example/dist/
	***************************************************************************/
	function getPath() { 
		var path = window.location.href;
		return path.substring(0, path.indexOf("zzfw")+4) + "/";
	}
	
	 