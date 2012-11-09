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
		<div class="ninecol">
			<p>Documentation is available <a href="documentation/Documentation.rtf">here</a>.</p>
		</div><!-- /.ninecol -->
		
		<div class="threecol last">

			<section class="mod box standardbox">
				<div class="mod-wrap">
					<div class="mod-hd">
						<h1>See also</h1>
					</div><!-- /.mod-hd -->
					<div class="mod-bd">
						<h2 class="first">Files</h2>
						<p><a href="documentation/Documentation.rtf">Documentation</a></p>
						
						<h2>Links</h2>
						<p><a href="validate-ws">Web service</a></p>
						<p><a href="validate-ws/application.wadl">Web service WADL</a></p>
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