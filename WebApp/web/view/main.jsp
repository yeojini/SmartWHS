<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>Admin Dashboard</title>

<!-- JQuery -->
<script src="https://code.jquery.com/jquery-latest.js"></script>
<!-- Bootstrap -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
	
<!-- Favicon icon -->
<link rel="icon" type="image/png" sizes="16x16"
	href="images/favicon.png">
<!-- Pignose Calender -->
<link href="./plugins/pg-calendar/css/pignose.calendar.min.css"
	rel="stylesheet">
<!-- Chartist -->
<link rel="stylesheet" href="./plugins/chartist/css/chartist.min.css">
<link rel="stylesheet"
	href="./plugins/chartist-plugin-tooltips/css/chartist-plugin-tooltip.css">
<!-- Custom Stylesheet -->
<link href="css/style.css?1234" rel="stylesheet">


<!-- Page plugins css -->
<link href="./plugins/clockpicker/dist/jquery-clockpicker.min.css"
	rel="stylesheet">
<!-- Color picker plugins css -->
<link href="./plugins/jquery-asColorPicker-master/css/asColorPicker.css"
	rel="stylesheet">
<!-- Date picker plugins css -->
<link href="./plugins/bootstrap-datepicker/bootstrap-datepicker.min.css"
	rel="stylesheet">
<link
	href="./plugins/bootstrap-material-datetimepicker/css/bootstrap-material-datetimepicker.css"
	rel="stylesheet">
<!-- Table -->
<link
	href="./plugins/tables/css/datatable/dataTables.bootstrap4.min.css"
	rel="stylesheet">
	

</head>

<body>

	<!--*******************
        Preloader start
    ********************-->
	<div id="preloader">
		<div class="loader">
			<svg class="circular" viewBox="25 25 50 50">
                <circle class="path" cx="50" cy="50" r="20" fill="none"
					stroke-width="3" stroke-miterlimit="10" />
            </svg>
		</div>
	</div>
	<!--*******************
        Preloader end
    ********************-->


	<!--**********************************
        Main wrapper start
    ***********************************-->
	<div id="main-wrapper">

		<!--**********************************
            Nav header start
        ***********************************-->

		<div class="nav-header">
			<div class="brand-logo">
				<a href="index.html"> <b class="logo-abbr"><img
						src="images/logo.png" alt=""> </b> <span class="logo-compact"><img
						src="./images/logo-compact.png" alt=""></span> <span
					class="brand-title"> <img src="images/logo-text.png" alt="">
				</span>
				</a>
			</div>
		</div>

		<!--**********************************
            Nav header end
        ***********************************-->

		<!--**********************************
            Header start
        ***********************************-->
		<div class="header">


			<!--**********************************
		           Menu start
		        ***********************************-->

			<div class="header-menu">
				<span> <!-- 상품 입출고 --> <a href="itpage.pc"
					aria-expanded="false" class="header-menu-list"> <i
						class="icon-note menu-icon"></i> <span class="nav-text">Stock
							Management</span>
				</a> <!-- 지게차 관리 --> <a href="flpage.pc" aria-expanded="false"
					class="header-menu-list"> <i class="icon-speedometer menu-icon"></i>
						<span class="nav-text">Forklift Management</span>
				</a> <!-- 솔루션 --> <a href="solpage.pc" aria-expanded="false"
					class="header-menu-list"> <i class="icon-globe-alt menu-icon"></i>
						<span class="nav-text">Solution</span>
				</a>
				</span>
			</div>

			<!--**********************************
		            Menu end
		        ***********************************-->


			<div class="header-right">
				<ul class="clearfix">
					<!-- 메일박스 -->
					<li class="icons dropdown"><a href="javascript:void(0)"
						data-toggle="dropdown"> <i class="mdi mdi-email-outline"></i>
							<!-- <span class="badge badge-pill gradient-1">3</span> -->
					</a></li>
					<!-- 알림 -->
					<li class="icons dropdown"><a href="javascript:void(0)"
						data-toggle="dropdown"> <i class="mdi mdi-bell-outline"></i> <!-- <span class="badge badge-pill gradient-2">3</span> -->
					</a></li>
					<!-- 개인 아이콘 -->
					<li class="icons dropdown">
						<div class="user-img c-pointer position-relative"
							data-toggle="dropdown">
							<span class="activity active"></span> <img
								src="images/user/1.png" height="40" width="40" alt="">
						</div>
						<div
							class="drop-down dropdown-profile animated fadeIn dropdown-menu">
							<div class="dropdown-content-body">
								<ul>
									<li><a href="app-profile.html"><i class="icon-user"></i>
											<span>Profile</span></a></li>
									<li><a href="javascript:void()"> <i
											class="icon-envelope-open"></i> <span>Inbox</span>
											<div class="badge gradient-3 badge-pill gradient-1">3</div>
									</a></li>

									<hr class="my-2">
									<li><a href="page-lock.html"><i class="icon-lock"></i>
											<span>Lock Screen</span></a></li>
									<li><a href="page-login.html"><i class="icon-key"></i>
											<span>Logout</span></a></li>
								</ul>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<!--**********************************
            Header end ti-comment-alt
        ***********************************-->



	<!--**********************************
            Content body start <Center>
        ***********************************-->
	<c:choose>

		<c:when test="${center == null }">
			<jsp:include page="itpage.jsp" />
		</c:when>
		<c:otherwise>
			<jsp:include page="${center }.jsp" />
		</c:otherwise>



	</c:choose>
	<!--**********************************
            Content body end
        ***********************************-->


	<!--**********************************
            Footer start
        ***********************************-->
	<div class="footer">
		<div class="copyright">
			<p>
				Copyright &copy; Designed & Developed by <a
					href="https://themeforest.net/user/quixlab">Quixlab</a> 2018
			</p>
		</div>
	</div>
	<!--**********************************
            Footer end
        ***********************************-->
	<!--**********************************
        Main wrapper end
    ***********************************-->

	<!--**********************************
        Scripts
    ***********************************-->
    
    
    <script src="plugins/common/common.min.js"></script>
	<script src="js/custom.min.js"></script>
	<script src="js/settings.js"></script>
	<script src="js/gleek.js"></script>
	<script src="js/styleSwitcher.js"></script>
	<!-- font awesome -->
	<script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>
	<!-- Chartjs -->
	<script src="./plugins/chart.js/Chart.bundle.min.js"></script>
	<!-- Circle progress -->
	<script src="./plugins/circle-progress/circle-progress.min.js"></script>
	<!-- Datamap -->
	<script src="./plugins/d3v3/index.js"></script>
	<script src="./plugins/topojson/topojson.min.js"></script>
	<script src="./plugins/datamaps/datamaps.world.min.js"></script>
	<!-- Morrisjs -->
	<script src="./plugins/raphael/raphael.min.js"></script>
	<script src="./plugins/morris/morris.min.js"></script>
	<!-- Pignose Calender -->
	<!-- <script src="./plugins/moment/moment.min.js"></script> -->
	<!-- <script src="./plugins/pg-calendar/js/pignose.calendar.min.js"></script> -->
	<!-- ChartistJS -->
	<script src="./plugins/chartist/js/chartist.min.js"></script>
	<script
		src="./plugins/chartist-plugin-tooltips/js/chartist-plugin-tooltip.min.js"></script>

	<script src="./js/dashboard/dashboard-1.js"></script>
	


	<script src="./plugins/moment/moment.js"></script>
	
	<!-- Clock Plugin JavaScript -->
	<script src="./plugins/clockpicker/dist/jquery-clockpicker.min.js"></script>
	<!-- Color Picker Plugin JavaScript -->
	 <script
		src="./plugins/jquery-asColorPicker-master/libs/jquery-asColor.js"></script>
	<script
		src="./plugins/jquery-asColorPicker-master/libs/jquery-asGradient.js"></script>
	<script
		src="./plugins/jquery-asColorPicker-master/dist/jquery-asColorPicker.min.js"></script>
	
	
	<!-- Date Picker Plugin JavaScript -->
	<script
		src="./plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script
		src="./plugins/bootstrap-material-datetimepicker/js/bootstrap-material-datetimepicker.js"></script>
	<script src="./js/plugins-init/form-pickers-init.js"></script>\
	
    
	<!-- Table -->
	<script src="./js/plugins-init/datatables.init-ex.js"></script>
	<script src="./plugins/tables/js/jquery.dataTables.min.js"></script>
	<!--<script src="./plugins/tables/js/datatable/dataTables.bootstrap4.min.js"></script>  -->
	<script src="./plugins/tables/js/datatable-init/datatable-basic.min.js?12"></script>
	<script src="./plugins/tables/js/datatable-init/datatable-api.min.js"></script>
	<script src="./plugins/tables/js/datatable-init/datatable-styling.min.js"></script>
	

	<!-- Modal -->
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</body>

</html>