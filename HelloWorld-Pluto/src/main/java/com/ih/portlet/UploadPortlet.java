package com.ih.portlet;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
public class UploadPortlet extends GenericPortlet {
	@Override
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
	}
	@Override
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		String fileName=request.getParameter("fileName");
		String fileSize=request.getParameter("fileSize");
		if(fileName!=null && !fileName.isEmpty())
		{
		response.getWriter().println("File Name: "+fileName +"<br/>File Size: "+fileSize);
		}
        response.getWriter().println("<form action="+response.createActionURL()+" method='POST' enctype='application/x-www-form-urlencoded'>"
                + "Enter message : <input type='text' id='message' name='message' value='"+(request.getParameter("message")==null?"":request.getParameter("message"))+"'/>"
                + "<input type='submit' value='Submit Form Message'/><br/>"
                + "</form>"
                + "<form action="+response.createActionURL()+" method='POST' enctype='multipart/form-data'>"
                + "Upload Your File: <input type='file' name='fileUpload'/>"
                + "<input type='submit' value='Upload File'/>"
                + "</form>");
 
    }
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        // Handle default MIME type request
        if(request.getContentType().equals("application/x-www-form-urlencoded")){
            String message = request.getParameter("message");
            
            System.out.println(message);
            response.setRenderParameter("message",message);
        }
        // Handle multipart request
        else if(request.getContentType().contains("multipart/form-data")){
            // Create FileItemFactory
            FileItemFactory factory = new DiskFileItemFactory();
            // Create PortletFileUpload instance
            PortletFileUpload fileUpload = new PortletFileUpload(factory);
            try {
                // Instead of parsing the request ourselves, let Apache PortletFileUpload do that
                List<FileItem> files = fileUpload.parseRequest(request);
                // Iterate over files
                for(FileItem item : files){
                	response.setRenderParameter("fileName", item.getName());
                	response.setRenderParameter("fileSize", String.valueOf(item.getSize()));
                    // Print out some of information
                    System.out.println("File Uploaded Name Is : "+item.getName()+" , Its Size Is :: "+item.getSize());
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }
    }
}
