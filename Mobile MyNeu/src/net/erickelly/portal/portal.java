package net.erickelly.portal;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.Bitmap;

public class portal extends Activity {
    
	private WebView webview;
	private String login = "http://myneu.neu.edu/cp/home/displaylogin";
	private String home = "http://myneu.neu.edu/render.userLayoutRootNode.uP?uP_root=root";
	private String logout = "http://myneu.neu.edu/cp/ip/login?sys=was&url=http://myneu.neu.edu/up/Logout";
	private String transactions = "http://myneu.neu.edu/cp/ip/login?sys=was&url=https://prod-web.neu.edu/webapp/ISF/cardTxns.do";
	private Boolean gotUUID;
	
	ProgressDialog dialog;
	Handler handle;
	Button loginButton;
	EditText username;
	EditText password;
	
	// Javascript code to run
	String styleJs =
		"document.body.style.padding = \"0 5%\";\n" + 
		"document.body.style.background = \"#eee\";\n";
	String loginJs = 
			"function login() {\n" + 
			"// LOGIN FORM USING SCRIPT FROM \"MyNEU PROPER LOGIN v0.6\" BY brainonfire.net \n" + 
			"	\n" + 
			"		var uuid = /document\\.cplogin\\.uuid\\.value=\"([0-9a-f-]{36})\";/.exec(document.getElementsByTagName('head')[0].innerHTML)[1];\n" + 
			"		\n" + 
			"		var submitTo = document.getElementsByName('cplogin')[0].action;\n" + 
			"		var submitTo_safe = submitTo.replace(/\"/g, '&quot;');\n" + 
			"		\n" + 
			"		var properForm =\n" + 
			"		'<form action=\"%FormAction%\" method=\"post\" id=\"loginform\"> \\\n" + 
			"		<input type=\"text\" name=\"user\" value=\"\" placeholder=\"MyNEU Username\" style=\"width: 100%; font-size: 18px;\"/><br> \\\n" + 
			"		<input type=\"password\" name=\"pass\" placeholder=\"Password\" style=\"width: 100%; font-size: 18px;\"/><br> \\\n" + 
			"		<input type=\"hidden\" name=\"uuid\" value=\"%UUID%\" /> \\\n" + 
			"		<button style=\"width:100%; height: 50px; font-size: 18px; margin-top:20px;\">Login</button> \\\n" + 
			"		</form>'.replace('%FormAction%', submitTo_safe).replace('%UUID%', uuid);\n" + 
			"		\n" + 
			"		document.body.innerHTML = properForm;\n" + 
			"}\n" +
			"login();\n" + styleJs +
			"android.showPage();\n";
	/*
	String loginJs = 
		"document.body = document.createElement('body');\n" + 
		// will not work due to 'security issues'
		// see: http://stackoverflow.com/questions/5649111/android-webview-loading-javascript-file-in-assets-folder 
		"document.body.background = 'file:///android_asset/background_plain.png';\n" +
		"android.showPage();"; */
	String transMenu = 
			"function transMenu() {\n" +
			"/* ACCOUNTS MENU */\n" + 
			"	\n" + 
			"       document.body = document.createElement('body');\n" + 
			"       document.body.style.margin = '20px';\n" + 
			"		links = [];\n" + 
			"		urls = {\n" + 
			"			'Husky Dollars': 'https://prod-web.neu.edu/webapp6/ISF/cardTxns.do?view=svc&page=0&sort=',\n" + 
			"			'Dining Dollars': 'https://prod-web.neu.edu/webapp6/ISF/cardTxns.do?view=meal&page=0&sort=',\n" + 
			"			'Laundry Bucks': 'https://prod-web.neu.edu/webapp6/ISF/cardTxns.do?view=laundry&page=0&sort=',\n" + 
			"			'Print Allowance': 'https://prod-web.neu.edu/webapp6/ISF/cardTxns.do?view=printing&page=0&sort=',\n" + 
			"		};\n" + 
			"		// write new content\n" + 
			"		for(var i in urls) {\n" + 
			"			links.push(document.createElement('a'));\n" + 
			"			links[links.length-1].href = urls[i];\n" + 
			"			links[links.length-1].innerHTML = '<div style=\"width:100%; height: 50px; text-align: center; line-height: 50px;" + 
			"			margin-bottom: 10px; -webkit-border-radius: 5px; color: white; font-weight: bold; text-shadow: 1px 1px #666;" + 
			"			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#C44), color-stop(100%,#933));" +
			"				\">' + i + '</div>';\n" +
			"			document.body.appendChild(links[links.length-1]);" +
			"		}\n" + 
			"       // Add space for the Back button\n" + 
			"		// document.body.appendChild(document.createElement('br'));\n" + 
			"		document.body.appendChild(document.createElement('br'));\n" + 
			"		var back = 'http://myneu.neu.edu/render.userLayoutRootNode.uP?uP_root=root';\n" + 
			"		var lo = document.createElement('a');\n" + 
			"		lo.href = back;\n" + 
			"		lo.innerHTML = '<div style=\"width:100%; height: 50px; " +
			"		-webkit-border-radius: 5px; color: white; font-weight: bold; text-shadow: 1px 1px #666;" + 
			"		background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#999), color-stop(100%,#333));" +
			"		text-align: center; line-height: 50px; margin-bottom: 10px;\">Back</div>';\n" + 
			"		document.body.appendChild(lo);\n" +
			"}\n" +
			"transMenu();\n" + styleJs +
			"android.showPage();";
	String portalJs = 
			"function portal() {\n" + 
			"/* MAIN MENU */\n" + 
			"	\n" + 
			"       document.body = document.createElement('body');\n" + 
			"       document.body.style.margin = '20px';\n" + 
			"		for(var i = 0; i < document.styleSheets.length - 1; i++) {\n" + 
			"			document.styleSheets[i] = null;\n" + 
			"		}\n" + 
			"		links = [];\n" + 
			"		urls = {\n" + 
			"			'Accounts': 'http://myneu.neu.edu/cp/ip/login?sys=was&url=https://prod-web.neu.edu/webapp6/HuskyCard/CurrentBalance/secure/retrieve/main.do',\n" + 
			"			'Transactions': 'http://myneu.neu.edu/cp/ip/login?sys=was&url=https://prod-web.neu.edu/webapp/ISF/cardTxns.do',\n" +
			"			'Mail': 'http://myneu.neu.edu/cp/ip/login?sys=google&url=http://mail.google.com/a/husky.neu.edu',\n" + 
			"		};\n" + 
			"		// write new content\n" + 
			"		for(var i in urls) {\n" + 
			"			links.push(document.createElement('a'));\n" + 
			"			links[links.length-1].href = urls[i];\n" + 
			"			links[links.length-1].innerHTML = '<div style=\"width:100%; height: 50px; text-align: center; line-height: 50px;" +
			"			margin-bottom: 10px; -webkit-border-radius: 5px; color: white; font-weight: bold; text-shadow: 1px 1px #666;" + 
			"			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#C44), color-stop(100%,#933));" +
			"				\">' + i + '</div>';\n" + 
			"			document.body.appendChild(links[links.length-1]);\n" + 
			"		}\n" + 
			"}\n" + 
			"portal();\n" + styleJs + 
			"android.showPage();";
	String currBalJs = 
		"function currBal() {\n" + 
		"	var titles = [\"Husky Dollars\",\"Dining Dollars\",\"Free Print Allowance\",\"Laundry Bucks\"];\n" + 
		"    var parent = document.getElementsByTagName('blockquote');\n" + 
		"    var tables = [];\n" + 
		"    for(var i = 0; i < parent.length; i++) {\n" + 
		"        var t = parent[i].getElementsByTagName('table')[0];\n" + 
		"        tables.push(t);\n" + 
		"    }\n" + 
		"    document.body = document.createElement('body');\n" + 
		"    for(var i in titles) {\n" + 
		"        var t = document.createElement('h3');\n" + 
		"        t.innerHTML = titles[i]; \n" + 
		"        document.body.appendChild(t);\n" + 
		"        document.body.appendChild(tables[i]);\n" + 
		"    }\n" + 
		"}\n" + 
		"currBal();\n" + styleJs +
		"android.showPage();";
	String formatTransactionPagesJs = 
		"function transactions() {\n" + 
		"	var start = document.getElementsByTagName('p')[3];\n" + 
		"    var tablerows = start.getElementsByTagName('tr');\n" + 
		"    document.body = document.createElement('body');\n" + 
		"    var t = document.createElement('table');\n" + 
		"    var tb = document.createElement('tbody');\n" + 
		"    tb.appendChild(tablerows[0]);\n" + 
		"    tb.appendChild(tablerows[1]);\n" + 
		"    t.appendChild(tb);\n" + 
		"    document.body.appendChild(t);\n" + 
		"}\n" + 
		"transactions();\n" +
		"android.showPage();";
	
	@Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add progress bar
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.main);
        
        // Set Views
        webview = (WebView) findViewById(R.id.webView1);
        
        // Set up webview
        webview.setVerticalScrollBarEnabled(false);
        webview.setHorizontalScrollBarEnabled(false);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new JsInterface(), "android");
        webview.setWebViewClient(new WebViewClient() {  
          @Override  
          public void onPageFinished(WebView view, String url){
        	  loadMyNeuJs();
          }
          @Override
          public void onPageStarted(WebView view, String url, Bitmap favicon) {
        	  webview.setVisibility(View.GONE);
          }
          /* CAN POSSIBLY CUT OUT PAGE LOADING WITH shouldOverrideUrlLoading(Webview view, String url) */
        });
        final Activity activity = this;
        webview.setWebChromeClient(new WebChromeClient() {
        	@Override
        	public void onProgressChanged(WebView view, int progress) {
        		activity.setProgress(progress * 100);
        	}
        });
        webview.getSettings().setBuiltInZoomControls(false);
        
        handle = new Handler();
        
        webview.loadUrl(login);
    }
	
	//javascript interface
    private class JsInterface{
    	// makes the webview visable
    	public void showPage(){
    		handle.post(new Runnable() {
    			public void run() {
    				webview.setVisibility(View.VISIBLE);
    			}
    		});
    	}
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
	    	String url = webview.getUrl();
	    	Log.d("URL equals", url);
	    	if(url.equals("https://prod-web.neu.edu/webapp6/ISF/cardTxns.do")) {
	    		webview.setVisibility(View.GONE);
	    		webview.loadUrl(home);
	    	} else if(url.contains((CharSequence)"displaylogin")) {
	    		webview.setVisibility(View.GONE);
	    		webview.loadUrl(login);
			} else if(url.contains((CharSequence)"cp/home/next")) {
				webview.setVisibility(View.GONE);
				webview.loadUrl(home);
			} else if(url.contains((CharSequence)"HuskyCard/CurrentBalance")) {
				webview.setVisibility(View.GONE);
				webview.loadUrl(home);
			} else if(url.contains((CharSequence)"cardTxns.do?view=")) {
				webview.setVisibility(View.GONE);
				webview.loadUrl(transactions);
			} else if(url.contains((CharSequence)"mail.google.com")) {
				webview.setVisibility(View.GONE);
				webview.loadUrl(home);
			} else {
				webview.setVisibility(View.GONE);
				webview.goBack();
			}
	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public void loadMyNeuJs() {
//	  	dialog.dismiss();
		String url = "";
		if (webview.getOriginalUrl() != null) {
			url = webview.getOriginalUrl();
		}
		Log.d("URL:", url);
		if(url.contains((CharSequence)"displaylogin") || (url == "")) {
			Log.d("loadMyNeuJs","called on login");
			reformatLogin();
		} else if(url.contains((CharSequence)"cp/home/next") || 
				url.contains((CharSequence) "render.userLayoutRootNode.uP?")) {
			Log.d("loadMyNeuJs","called on portal");
			Log.d("JS=", portalJs);
			formatPortal();
		} else if(url.contains((CharSequence)"HuskyCard/CurrentBalance")) {
			formatCurrBal();
		} else if(url.contains((CharSequence)"jsp/misc")) {
			Log.d("LoadMyNeuJs","something went wrong");
			webview.loadUrl(login);
		} else if(url.contains((CharSequence)"cardTxns.do")) {
			formatTransactions();
		} else {
			Log.d("loadMyNeuJs", "called on other page");
      	  	webview.setVisibility(View.VISIBLE);
		}
	}
	
	private void formatTransactions() {
		
		if(webview.getUrl().contains((CharSequence)"?view=")) {
			webview.loadUrl("javascript:" + formatTransactionPagesJs);
			Log.d("formatTransactions","formatted page");
		} else {
			webview.loadUrl("javascript:" + transMenu);
		}
		Log.d("formatTransactions","ran");
	}

	private void formatCurrBal() {
		webview.loadUrl("javascript:" + currBalJs);
		Log.d("formatCurrBal", "called");
	}

	public void reformatLogin() {
		webview.loadUrl("javascript:" + loginJs);
		Log.d("reformatLogin","ran");
	}
	
	public void formatPortal() {
		webview.loadUrl("javascript:" + portalJs);
		Log.d("formatPortal","ran");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.menu, menu);       
       return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case R.id.logout:
    			webview.setVisibility(View.GONE);
    			webview.loadUrl(logout);
    			webview.setVisibility(View.GONE);
    			webview.loadUrl(login);
    			break;
    		case R.id.home:
    			webview.setVisibility(View.GONE);
    			webview.loadUrl(home);
    			break;
    	}
    	   return true;
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
	}
	
}