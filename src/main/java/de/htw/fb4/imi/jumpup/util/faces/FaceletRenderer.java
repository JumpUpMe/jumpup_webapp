/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.util.faces;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.view.ViewDeclarationLanguage;

import de.htw.fb4.imi.jumpup.ApplicationError;

/**
 * <p>Facade to render Facelets programmatically.</p>
 * 
 * <p>It can be used in order to render JSF templates within eMails.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 28.11.2014
 *
 */
public class FaceletRenderer
{
    protected ResponseWriter originalWriter;
    private FacesContext facesContext;
    
   
    public FaceletRenderer(FacesContext facesContext)
    {
        this.facesContext = facesContext;
    }
    
    /**
     * 
     * @param pathToFacelet the path to the facelet
     * @return the rendered view as String
     * 
     * @throws IllegalStateException
     * @throws {@link ApplicationError}
     */
    public String renderView(final String pathToFacelet) 
    {
        if (null == this.facesContext) {
            throw new IllegalStateException("FaceletRenderer: No faces context injected");
        }
        
        this.storeOriginalResponseWriter();
        
        try {            
            StringWriter stringWriter = new StringWriter();
            ResponseWriter inMemoryResponseWriter = this.writeResponseInMemory(stringWriter);
            this.facesContext.setResponseWriter(inMemoryResponseWriter);
            
            UIViewRoot view = this.createView(pathToFacelet);  
            
            this.renderFacelet(pathToFacelet, view);
            
            return stringWriter.toString();
        } catch (IOException e) {
            throw new ApplicationError("renderView(): Could not render facelet", getClass());
        } finally {
            this.restoreOriginalResponseWriter();
        }
    }  

    /**
     * Render the given facelet.
     * @param pathToFacelet
     * @param view
     * @throws IOException
     */
    private void renderFacelet(String pathToFacelet, UIViewRoot view) throws IOException
    {
        ViewHandler viewHandler = this.facesContext.getApplication().getViewHandler();
        ViewDeclarationLanguage viewDeclarationLangauge = viewHandler.getViewDeclarationLanguage(this.facesContext, pathToFacelet);
        viewDeclarationLangauge.buildView(this.facesContext, view);
        
        this.renderChildren(view);
    }

    /**
     * Render each child UI component.
     * @param view
     * @throws IOException
     */
    private void renderChildren(UIViewRoot view) throws IOException
    {
        List<UIComponent> viewChildren = view.getChildren();
        
        for(UIComponent child: viewChildren) {
            this.renderChild(child);
        }
    }

    /**
     * Render the child UI component.
     * @param child
     * @throws IOException
     */
    private void renderChild(UIComponent child) throws IOException
    {
        // Render-Response phase rendering?
        if (child.isRendered()) {
            child.encodeAll(this.facesContext);
        }
    }

    private UIViewRoot createView(String pathToFacelet)
    {
        ViewHandler viewHander = this.facesContext.getApplication().getViewHandler();
        return viewHander.createView(this.facesContext, pathToFacelet);        
    }

    private ResponseWriter writeResponseInMemory(StringWriter responseWriter)
    {
        ExternalContext externalContext = this.facesContext.getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        String contentType = (String) requestMap.get("facelets.ContentType");
        String encoding = (String) requestMap.get("facelets.Encoding");
        
        RenderKit renderKit = this.facesContext.getRenderKit();
        return renderKit.createResponseWriter(responseWriter, contentType, encoding);        
    }

    protected void storeOriginalResponseWriter()
    {
        try {
            this.originalWriter = facesContext.getResponseWriter();
        } catch (IllegalStateException e) {
            // ok
        }
    }
    
    private void restoreOriginalResponseWriter()
    {
        // TODO could lead to a bug but i dont care
        if (null != this.originalWriter) {
            this.facesContext.setResponseWriter(this.originalWriter);
        }
    }
}
