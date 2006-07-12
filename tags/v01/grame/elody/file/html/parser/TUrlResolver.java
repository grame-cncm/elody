package grame.elody.file.html.parser;

import java.net.MalformedURLException;
import java.net.URL;

public final class TUrlResolver {
	static void SplitURL(URL url, StringBuffer path, StringBuffer doc)
    {
        String urlInfo;
        int idx;
        if (path != null)
            path.setLength(0);
        if (doc != null)
            doc.setLength(0);
        urlInfo = url.getFile();
        if (urlInfo.length() == 0)
            return;
        String urlDoc = null;
        idx = urlInfo.lastIndexOf(47);
        if (idx != -1)
            urlDoc = urlInfo.substring(idx + 1);
        else
            urlDoc = urlInfo;
        if (urlDoc.length() > 0)
        {
            if (urlDoc.lastIndexOf(46) == -1)
                idx = urlInfo.length();
            else if (doc != null)
                doc.append(urlDoc);
        }
        if (idx > 0)
        {
            String urlPath = urlInfo.substring(0, idx);
            if (urlPath.length() > 0 && path != null)
                path.append(urlPath);
        }
    }

    static URL ResolveURL(URL context, String url)
        throws MalformedURLException
    {
        if (url.length() == 0)
            throw new MalformedURLException();
        if (url.indexOf(":/") != -1)
            return new URL(url);
        if (url.charAt(0) == 47)
            return new URL(context, url);
        else
            return new URL(context.toString() + url);
    }


	// Methoode publique
	
	static public URL makeUrl (URL doc, String str) throws MalformedURLException {
	
		URL context = null;
        StringBuffer path = new StringBuffer();
        SplitURL(doc, path, null);
        
        if (path.length() == 0)
            context = new URL(doc, "/");
        else{
    		context = new URL(doc, path.toString() + "/");
    	}
    
 		URL url = ResolveURL(context, str);
       
      	return url;
               
	}
}
