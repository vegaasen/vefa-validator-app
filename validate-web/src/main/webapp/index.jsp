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
		<div class="eightcol">
			<p>The VEFA Validator is a program used to validate EHF xml files. The validator reads an XML and validates it 
			against a set of validation steps. For each of these validation steps the validator accumulates potential warnings and errors. 
			After the validator is finished validating the XML it returns a XML of potential messages.</p>
		</div><!-- /.twelvecol -->
		
		<div class="fourcol last">
		
			<section class="mod box standardbox">
				<div class="mod-wrap">
					<div class="mod-hd">
						<h1>See also</h1>
					</div><!-- /.mod-hd -->
					<div class="mod-bd">
						<h2 class="first">Links</h2>
						<p><a href="http://anskaffelser.no/e-handel/faktura">Electronic invoice</a></p>
						<p><a href="http://anskaffelser.no/e-handel/faktura/teknisk-informasjon">Technical information</a></p>
						<p><a href="http://anskaffelser.no/e-handel/faktura/nyheter">News</a></p>						
					</div><!-- /.mod-bd -->
				</div><!-- /.mod-wrap -->
			</section>
					
		</div>
	</div><!-- /.row -->

</div><!-- /.container main -->

<jsp:include page="includes/footer.jsp"/>

<jsp:include page="includes/js.jsp"/>

<script type="text/javascript">
  $(document).ready(function(){
  });
</script>
<!-- end scripts -->

</body>
<jsp:include page="includes/html_stop.jsp"/>
