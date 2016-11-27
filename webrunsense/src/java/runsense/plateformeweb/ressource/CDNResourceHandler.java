/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.ressource;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ResourceWrapper;

/**
 *
 * @author runsense
 */

    public class CDNResourceHandler extends ResourceHandlerWrapper {

    private ResourceHandler wrapped;

    public CDNResourceHandler(ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Resource createResource(final String resourceName, final String libraryName) {
        final Resource resource = super.createResource(resourceName, libraryName);

        if (resource == null || !"primefaces".equals(libraryName) || !"jquery/jquery.js".equals(resourceName)) {
            return resource;
        }

        return new ResourceWrapper() {

            @Override
            public String getRequestPath() {
                return "http://mydomain.com/jquery/jquery.js";
            }

            @Override
            public Resource getWrapped() {
                return resource;
            }
        };
    }

    @Override
    public ResourceHandler getWrapped() {
        return wrapped;
    }

}
    

