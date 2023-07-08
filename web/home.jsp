

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
   // HttpSession session = request.getSession();
    String getPath = (String) request.getParameter("page");
    if(getPath == null) getPath = "dashboard";
    String theFile = getPath + ".jsp";
    
%>

<!DOCTYPE html>
<html>
    <head>
          <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title> MAIZE Admin</title>
        <!-- plugins:css -->
        <link rel="stylesheet" href="asset/vendors/feather/feather.css">
        <link rel="stylesheet" href="asset/vendors/ti-icons/css/themify-icons.css">
        <link rel="stylesheet" href="asset/vendors/css/vendor.bundle.base.css">
        <!-- endinject -->
        <!-- Plugin css for this page -->
        <link rel="stylesheet" href="asset/vendors/select2/select2.min.css">
        <link rel="stylesheet" href="asset/vendors/select2-bootstrap-theme/select2-bootstrap.min.css">
        <link rel="stylesheet" href="asset/vendors/datatables.net-bs4/dataTables.bootstrap4.css">
        <link rel="stylesheet" href="asset/vendors/ti-icons/css/themify-icons.css">
        <link rel="stylesheet" type="text/css" href="asset/js/select.dataTables.min.css">
        <!-- End plugin css for this page -->
        <!-- inject:css -->
        <link rel="stylesheet" href="asset/css/vertical-layout-light/style.css">
        <!-- endinject -->
    </head>

    <body>
        <!-- ============================================================== -->
        <!-- main wrapper -->
        <!-- ============================================================== -->
        <div class="container-scroller">
            <!-- ============================================================== -->
            <!-- Header -->
            <!-- ============================================================== -->     
            <%@ include file = "header.jsp" %>
            <!-- ============================================================== -->
            <!-- End header -->
            <!-- ============================================================== -->     
            <%--<%@ include file = "%{getPath}.jsp" %>--%>
            <jsp:include page="<%= theFile %>"/>

                </div>
                <!-- content-wrapper ends -->
                <!-- partial:partials/_footer.html -->
                <footer class="footer">
                  <div class="d-sm-flex justify-content-center justify-content-sm-between">
                    <span class="text-muted text-center text-sm-left d-block d-sm-inline-block">Copyright © 2021.  Premium <a href="https://www.bootstrapdash.com/" target="_blank">Bootstrap admin template</a> from BootstrapDash. All rights reserved.</span>
                    <span class="float-none float-sm-right d-block mt-1 mt-sm-0 text-center">Hand-crafted & made with <i class="ti-heart text-danger ml-1"></i></span>
                  </div>
                </footer>
                <!-- partial -->
              </div>
              <!-- main-panel ends -->
            </div>
            <!-- page-body-wrapper ends -->
        </div>
      <!-- container-scroller -->

  <!-- plugins:js -->
  <script src="asset/vendors/js/vendor.bundle.base.js"></script>
  <!-- endinject -->
  <!-- Plugin js for this page -->
  <script src="asset/vendors/chart.js/Chart.min.js"></script>
  <script src="asset/vendors/datatables.net/jquery.dataTables.js"></script>
  <script src="asset/vendors/datatables.net-bs4/dataTables.bootstrap4.js"></script>
  <script src="asset/js/dataTables.select.min.js"></script>

  <!-- End plugin js for this page -->
  <!-- inject:js -->
  <script src="asset/js/off-canvas.js"></script>
  <script src="asset/js/hoverable-collapse.js"></script>
  <script src="asset/js/template.js"></script>
  <script src="asset/js/settings.js"></script>
  <script src="asset/js/todolist.js"></script>
  <!-- endinject -->
  <!-- Custom js for this page-->
  <script src="asset/js/dashboard.js"></script>
  <!-- End custom js for this page-->
</body>

</html>
