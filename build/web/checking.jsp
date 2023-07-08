<%@page import="java.sql.*"%>
<%@page import="model.*"%>
<%@page import="java.util.*"%>

<% List<Parcel> parcels = (List<Parcel>) request.getAttribute("parcels"); %>

<div class="row justify-content-between">
    <div class="col-lg-8 col-md-8">
        <div class="card">
            <div class="card-body">
                <div class="card-title" style="margin-bottom: 50px;">
                    <h3 class="title-3"> New parcel check </h3>
                    <!--<span style="color: red"> Date invalid </span>-->
                </div>
                
                <form action="Add_check_ctrl" method="get" novalidate="novalidate">
                    <!-- CHOOSING PARCEL --> 
                    <div class="row form-group">
                        <div class="col col-md-3">
                            <label for="selectSm" class=" form-control-label"> Choose parcel  :</label>
                        </div>
                        <div class="col-12 col-md-9">
                            <select name="parcel_id" id="SelectLm" class="form-control-sm form-control">
                                <% for(Parcel p : parcels){ %>
                                    <option value="<%= p.getId_parcel() %>"> <%= p.getName_parcel() %> </option>
                                <% } %>
                            </select>
                        </div>
                    </div>

                    <!-- DATE OF CHECKING --> 
                    <div class="row form-group">
                        <div class="col col-md-3">
                            <label for="selectSm" class=" form-control-label"> Checking date : </label>
                        </div>
                        <div class="col-12 col-md-9">
                            <input id="cc-pament" name="date_checking" type="date" class="form-control" aria-required="true" aria-invalid="false">
                        </div>
                    </div>

                    <!-- AMOUNT EXACT OF THE FEET --> 
                    <div class="row form-group">
                        <div class="col col-md-3">
                            <label for="selectSm" class=" form-control-label"> Feet amount : </label>
                        </div>
                        <div class="col-12 col-md-9">
                            <input id="cc-pament" name="feet_amount" type="number" class="form-control" aria-required="true" aria-invalid="false">
                        </div>
                    </div>

                    <!-- AVERAGE OF CORNCOB --> 
                    <div class="row form-group">
                        <div class="col col-md-3">
                            <label for="selectSm" class=" form-control-label"> Average corncob : </label>
                        </div>
                        <div class="col-12 col-md-9">
                            <input id="cc-pament" name="corncob" type="number" class="form-control" aria-required="true" aria-invalid="false">
                        </div>
                    </div>
                    
                    <!-- COLOR --> 
                    <div class="row form-group">
                        <div class="col col-md-3">
                            <label for="selectSm" class=" form-control-label"> Color : </label>
                        </div>
                        <div class="col-12 col-md-9">
                            <input id="cc-pament" name="color_corn" type="number" class="form-control" aria-required="true" aria-invalid="false">
                        </div>
                    </div>
                    
                    <!-- GROWTH --> 
                    <div class="row form-group">
                        <div class="col col-md-3">
                            <label for="selectSm" class=" form-control-label"> Growth : </label>
                        </div>
                        <div class="col-12 col-md-9">
                            <input id="cc-pament" name="growth_corn" type="number" class="form-control" aria-required="true" aria-invalid="false">
                        </div>
                    </div>
                    
                    <div>
                        <button id="payment-button" type="submit" class="btn  btn-info">
                            Save
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="col-md-4 col-lg-4 grid-margin stretch">
        <div class="card-people mt-auto" style="padding: 0% 0% 0% 0%">
          <img src="asset/images/corn6.jpg" alt="people">
        </div>
    </div>
</div>
