package com.abcm.esign_service.dyanamicRequestBody;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ZoopEsignAdhaarRequest {
	
	 private Document document;
	    private List<Signer> signers;
	    private String txn_expiry_min;
	    private String white_label;
	    private String redirect_url;
	    private String response_url;
	    private String esign_type;
	    private EmailTemplate email_template;

	   

	    @Data
	    @ToString
	    public static class Document {
	        private String name;
	        private String data;
	        private String info;

	       
	    }

	    @Data
	    @ToString
	    public static class Signer {
	        private String signer_name;
	       // private String signer_email;
	        private String signer_purpose;
	        private List<SignCoordinates> sign_coordinates;

	       
	    }

	    @Data
	    @ToString
	    public static class SignCoordinates {
	        private int page_num;
	        private int x_coord;
	        private int y_coord;

	       
	    }

	    @Data
	    @ToString
	    public static class EmailTemplate {
	        private String org_name;

	       
	    }

}
