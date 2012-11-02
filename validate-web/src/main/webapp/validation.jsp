<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<jsp:include page="includes/html_start.jsp"/>
<jsp:include page="includes/head.jsp"/>
<body>

<jsp:include page="includes/top.jsp"/>

<jsp:include page="includes/header.jsp"/>

<jsp:include page="includes/menu.jsp"/>

<div class="container main">

	<div class="row submenues">
	   <!-- the submenus in the nav element are moved to this element with Javascript for none mobil screens. -->
	</div><!-- /.row submenues -->

	<div class="row">
		<div class="twelvecol last">
			<p>This tool allows you to validate an XML sample against different sets of business rules. </p>

			<ol>
				<li><em>Choose the business rule set you wish to validate against from the select box</em></li>
				<li><em>Copy and paste the XML into the text input box</em></li>
				<li><em>Click the 'Validate XML' button</em></li>
				<li><em>The results of the validation will be displayed underneath the text input box</em></li>
			</ol>

			<p>Please note that this tool validates whether the submitted text is well-formed XML, but it does not validate against an XSD Schema.</p>
			<!--p>Currently there are validations for the Invoice and Credit Note documents.</p-->


			<form id="xmlSourceForm" action="#">

				<label for="xsltSelect">Validation artifact:</label>
				<select id="xsltSelect"></select>

				<label for="xmlTextSource">Paste XML data:</label>
				<textarea id="xmlTextSource" rows="10" cols="100"></textarea>
			</form>

			<button id="readFileButton">Validate XML</button>

			<div id="transformResult"></div>

		</div><!-- /.ninecol -->
	</div><!-- /.row -->

</div><!-- /.container main -->

<jsp:include page="includes/footer.jsp"/>

<jsp:include page="includes/js.jsp"/>

<script type="text/javascript">
	$(document).ready(function(){
		var wsUrl = '/validate-ws';
		
		// Get available versions
		$.ajax({
			url: wsUrl,
			dataType: 'xml',
			success: getVersions
		});				
		
		function getVersions(xml){
			$(xml).find('version').each(function(){
				var version = $(this).text();
				
				// Get available schemas for version
				$.ajax({
					url: wsUrl + '/' + version,
					dataType: 'xml',
					success: getSchemas
				});
				
				function getSchemas(xml){
					$(xml).find('schema').each(function(){
						var id = $(this).attr('id');
						var href = $(this).attr('xlink:href');
						
						$(this).find('name').each(function(){
							var value = $(this).find('en').text();
							
							$('#xsltSelect').append($("<option></option>").attr("value",version + '/' + id).text(version + ' - ' + value)); 
						});
					});			
				};
				
			});
		}		
		
		// Send XML to ws and prosess result
		$("#readFileButton").click(function() {
			var url = wsUrl + '/' + escape($('#xsltSelect :selected').val());
			
			$.ajax({
				url: url,
				dataType: 'xml',
				type: 'POST',
				data: $('#xmlTextSource').val(),
				//data: '<test></test>',
				processData: false,
				contentType: 'application/xml',
				success: getResult
			});			
		});		
		
		function getResult(xml){
			var r = '<div style="height: 20px;"></div>';
			r += '<h2>' + $('#xsltSelect :selected').text() + '</h2>';
			
			$(xml).find('message').each(function(){								
				var schema = $(this).attr('schema');
				var validationType = $(this).attr('validationType');
				var version = $(this).attr('version');
				
				var messageType = $(this).find('messageType').text();
				var title = $(this).find('title').text();
				var description = $(this).find('description').text();	
				
				var style = '';
				if (messageType == 'FatalError') {
					style = 'color: red;'	
				} else if (messageType == 'Fatal') {
					style = 'color: red;'
				} else if (messageType == 'Warning') {
					style = 'color: black;'
				}
				
				r += '<h3 style="' + style + '" title="' + messageType + '">' + title + '</h3>';
				r += '<p>' + description + '</p>';
			});
			
			$('#transformResult').html(r);
		};
	});
</script>
<!-- end scripts -->

</body>
<jsp:include page="includes/html_stop.jsp"/>