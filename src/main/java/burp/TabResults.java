package burp;

import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;

import burp.IMessageEditorController;
import burp.IMessageEditorTab;
import ca.zhack.endpointfinder.EndpointEntry;
import ca.zhack.endpointfinder.EndpointFinder;
import ca.zhack.endpointfinder.EndpointResult;

public class TabResults implements IMessageEditorTab {
	private ITextEditor displayContent;
	private byte[] currentMessage;
	
	public TabResults(IBurpExtenderCallbacks callbacks) {
		displayContent = callbacks.createTextEditor();
		displayContent.setEditable(false);
	}
	
	public byte[] getMessage() {
		return currentMessage;
	}

	public byte[] getSelectedData() {
		return currentMessage;
	}

	public String getTabCaption() {
		return "Endpoints";
	}

	public Component getUiComponent() {
		return displayContent.getComponent();
	}

	public boolean isEnabled(byte[] content, boolean isRequest) {
		String fullContent = new String(content);
		String[] mainParts = fullContent.split("\r\n\r\n");
		String[] headers = mainParts[0].split("\r\n");
		
		for (String header : headers) {
			String[] parts = header.split(": ");
			
			if (parts.length < 2) {
				continue;
			}
			
			String headerName = parts[0];
			String headerValue = parts[1];
			
			if (headerName.toLowerCase().equals("content-type") &&
					headerValue.toLowerCase().contains("javascript")) {
				
				return true;
			}
		}
		
		return false;
	}

	public boolean isModified() {
		return false;
	}

	public void setMessage(byte[] message, boolean isRequest) {
		this.currentMessage = message;
		
		String httpContent = new String(message);
		int startContent = httpContent.indexOf("\r\n\r\n");
		
		if (startContent > 0) {
			try {
				StringBuilder display = new StringBuilder();
				EndpointResult result = EndpointFinder.getEndpoints(httpContent.substring(startContent) + 4);
				List<EndpointEntry> entries = result.getEntries();
				
				display.append("Results (" + entries.size() + ")");
				
				for (EndpointEntry entry : entries) {
					display.append("\n\n--------------------\n\n");
					display.append("Path : " + entry.getPath());
				}
				
				display.append("\n\n--------------------\n\n");
				displayContent.setText(display.toString().getBytes());
			} catch (Exception e) {
				String errMessage = "An error occured during the parsing of the content.\n\n";
				errMessage += e.getMessage();
				displayContent.setText(errMessage.getBytes());
			}
		} else {
			displayContent.setText("No content found in the body.".getBytes());
		}
	}

}
