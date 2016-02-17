/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.controller;

import javax.inject.Inject;

import de.htw.fb4.imi.jumpup.util.faces.FacesFacade;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 10.11.2014
 *
 */
public class AbstractFacesController
{   

    
    @Inject
    protected FacesFacade facesFacade;
    
    protected void addDisplayInfoMessage(final String infoMessage)
    {
        facesFacade.addInfoMessage(infoMessage);
    }
    
    protected void addDisplayErrorMessage(final String errorMessage)
    {
        facesFacade.addErrorMessage(errorMessage);
    }
}
