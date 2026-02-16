<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:eval expression="@environment.getProperty('ContextPath', 'https://default.url')" var="url" />
<!-- [ Main Content ] end -->
        <footer class="pc-footer">
            <div class="footer-wrapper container-fluid">
                <div class="row">
                    <div class="col-md-8 my-1 col-sm-12">
                        <p class="m-0" style="text-transform: capitalize">copyright &copy; 2025 All Right Reserved Designed & developed by <a href="https://www.abcmapp.com" target="_blank"> ABCM APP PVT LTD</a></p>
                    </div>
                    <div class="col-sm-12 col-md-4 my-1">
                        <ul class="list-inline footer-link mb-0">
                            <li class="list-inline-item"><a href="Dashboard.html">Home</a></li>
                            
                            <li class="list-inline-item"><a href="#" target="_blank">Support</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </footer>
       

        <!-- [Page Specific JS] start -->
        <script src="${url}/assets/js/plugins/apexcharts.min.js"></script>
        <!-- custom widget js -->
        <script src="${url}/assets/js/widgets/new-orders-graph.js"></script>
        <script src="${url}/assets/js/widgets/new-users-graph.js"></script>
        <script src="${url}/assets/js/widgets/visitors-graph.js"></script>
        <script src="${url}/assets/js/widgets/overview-chart.js"></script>
        <script src="${url}/assets/js/widgets/income-graph.js"></script>
        <script src="${url}/assets/js/widgets/languages-graph.js"></script>
        <script src="${url}/assets/js/widgets/overview-product-graph.js"></script>
        <script src="${url}/assets/js/widgets/total-earning-graph-1.js"></script>
        <script src="${url}/assets/js/widgets/total-earning-graph-2.js"></script>
        <script src="${url}/assets/js/plugins/apexcharts.min.js"></script>
        <!-- custom widgets js -->
        <script src="${url}/assets/js/widgets/total-line-1-chart.js"></script>
        <script src="${url}/assets/js/widgets/total-line-2-chart.js"></script>
        <script src="${url}/assets/js/widgets/total-line-3-chart.js"></script>
        <script src="${url}/assets/js/widgets/cashflow-bar-chart.js"></script>
        <script src="${url}/assets/js/widgets/category-donut-chart.js"></script>
        
        
         <script src="${url}/assets/js/plugins/apexcharts.min.js"></script>
        <script src="${url}/assets/js/widgets/all-earnings-graph.js"></script>
        <script src="${url}/assets/js/widgets/page-views-graph.js"></script>
        <script src="${url}/assets/js/widgets/total-task-graph.js"></script>
        <script src="${url}/assets/js/widgets/download-graph.js"></script>
        <script src="${url}/assets/js/widgets/customer-rate-graph.js"></script>
        <script src="${url}/assets/js/widgets/tasks-graph.js"></script>
        <script src="${url}/assets/js/widgets/total-income-graph.js"></script>
        
        <!-- [Page Specific JS] end -->
        <!-- Required Js -->
        <script src="${url}/assets/js/plugins/popper.min.js"></script>
        <script src="${url}/assets/js/plugins/simplebar.min.js"></script>
        <script src="${url}/assets/js/plugins/bootstrap.min.js"></script>

        <script src="${url}/assets/js/plugins/i18next.min.js"></script>
        <script src="${url}/assets/js/plugins/i18nextHttpBackend.min.js"></script>

        <script src="${url}/assets/js/icon/custom-font.js"></script>
        <script src="${url}/assets/js/script.js"></script>
        <script src="${url}/assets/js/theme.js"></script>
        <script src="${url}/assets/js/multi-lang.js"></script>
        <script src="${url}/assets/js/plugins/feather.min.js"></script>
         <script src="${url}/assets/js/plugins/vanillatree.min.js"></script>
        <script src="${url}/assets/js/elements/ac-treeview.js"></script>
        <script src="${url}/assets/js/plugins/choices.min.js"></script>
        <script src="${url}/assets/js/plugins/flatpickr.min.js"></script>
         <script src="${url}/assets/js/ajaxloaderstart_stop.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
         <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        
      


<script>
    // Get today's date
    var today = new Date();
    var formattedToday = flatpickr.formatDate(today, "d-m-Y");

    // ✅ Picker 1: Allow only today & past dates; future dates disabled (but viewable)
    flatpickr(document.querySelector('#pc-date_range_picker-2'), {
        mode: 'range',
        dateFormat: 'd-m-Y',
        defaultDate: [today, today],
        disable: [
            function(date) {
                return date > today; // Disable future dates
            }
        ],
        onReady: function () {
            document.getElementById('pc-date_range_picker-2').value = formattedToday + " to " + formattedToday;
        },
        onChange: function (selectedDates) {
            if (selectedDates.length === 2) {
                const from = flatpickr.formatDate(selectedDates[0], "d-m-Y");
                const to = flatpickr.formatDate(selectedDates[1], "d-m-Y");
                document.getElementById('pc-date_range_picker-2').value = from + " to " + to;
            }
        }
    });

    // ✅ Picker 2: Allow only dates divisible by 8 and not future
    flatpickr(document.querySelector('#pc-date_range_picker-3'), {
        mode: 'range',
        dateFormat: 'd-m-Y',
        defaultDate: [today, today],
        disable: [
            function(date) {
                return date.getDate() % 8 !== 0 || date > today;
            }
        ],
        onReady: function () {
            document.getElementById('pc-date_range_picker-3').value = formattedToday + " to " + formattedToday;
        },
        onChange: function (selectedDates) {
            if (selectedDates.length === 2) {
                const from = flatpickr.formatDate(selectedDates[0], "d-m-Y");
                const to = flatpickr.formatDate(selectedDates[1], "d-m-Y");
                document.getElementById('pc-date_range_picker-3').value = from + " to " + to;
            }
        }
    });
</script>


        
    </body>
    <!-- [Body] end -->
</html>
