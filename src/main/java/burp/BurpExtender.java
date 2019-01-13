package burp;

import burp.IBurpExtender;
import burp.IBurpExtenderCallbacks;
import burp.IMessageEditorController;
import burp.IMessageEditorTab;
import burp.IMessageEditorTabFactory;

public class BurpExtender implements IBurpExtender, IMessageEditorTabFactory {
	private IBurpExtenderCallbacks callbacks;
	
	public IMessageEditorTab createNewInstance(IMessageEditorController controller, boolean editable) {
		return new TabResults(this.callbacks);
	}

	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		this.callbacks = callbacks;
		this.callbacks.setExtensionName("EndpointFinder");
		this.callbacks.registerMessageEditorTabFactory(this);
	}
}
