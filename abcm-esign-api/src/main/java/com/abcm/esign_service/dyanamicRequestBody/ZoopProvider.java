package com.abcm.esign_service.dyanamicRequestBody;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abcm.esign_service.DTO.EsignMerchantRequest;
import com.abcm.esign_service.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Service("zoop-esign")
@RequiredArgsConstructor
public class ZoopProvider implements ServiceProvider<ZoopEsignAdhaarRequest> {

    @Override
    public ZoopEsignAdhaarRequest buildRequest(EsignMerchantRequest request, MultipartFile file) {

        ZoopEsignAdhaarRequest zoopRequest = new ZoopEsignAdhaarRequest();
        
       

        // ✅ 1. Document Set
        ZoopEsignAdhaarRequest.Document document = new ZoopEsignAdhaarRequest.Document();
        document.setName(request.getDocument_name());
        document.setData(CommonUtils.convertDocumentToBase64(file));
        document.setInfo("test");

        zoopRequest.setDocument(document);

        // ✅ 2. Signers Set
        List<ZoopEsignAdhaarRequest.Signer> signerList =
                request.getSigners().stream().map(signer -> {

                    ZoopEsignAdhaarRequest.Signer signerDto =
                            new ZoopEsignAdhaarRequest.Signer();
                    signerDto.setSigner_name(signer.getSigner_name());
                   // signerDto.setSigner_email(signer.getSigner_email());
                    signerDto.setSigner_purpose(signer.getSigner_purpose());
                    // Coordinates
                    List<ZoopEsignAdhaarRequest.SignCoordinates> coordinates =
                            signer.getSign_coordinates().stream().map(coord -> {

                                ZoopEsignAdhaarRequest.SignCoordinates sc =
                                        new ZoopEsignAdhaarRequest.SignCoordinates();

                                sc.setPage_num(coord.getPage_num());
                                sc.setX_coord(coord.getX_coord());
                                sc.setY_coord(coord.getY_coord());

                                return sc;

                            }).collect(Collectors.toList());

                    signerDto.setSign_coordinates(coordinates);

                    return signerDto;

                }).collect(Collectors.toList());

        zoopRequest.setSigners(signerList);

        // ✅ 3. Other Fields
        zoopRequest.setTxn_expiry_min(request.getLink_expiry_min());
        zoopRequest.setWhite_label("Y");
        zoopRequest.setRedirect_url("http://203.192.228.125:7081/WNPApp/esuccess");
        zoopRequest.setResponse_url("http://203.192.228.125:7081/ABCMKyc/esign/webhook");
        zoopRequest.setEsign_type("AADHAAR");

        // ✅ 4. Email Template
        ZoopEsignAdhaarRequest.EmailTemplate emailTemplate =
                new ZoopEsignAdhaarRequest.EmailTemplate();

        emailTemplate.setOrg_name("AbcmApp PVT LTD");

        zoopRequest.setEmail_template(emailTemplate);

        return zoopRequest;
    }
}
