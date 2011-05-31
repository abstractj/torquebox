package org.torquebox.core.as;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ModelNodeRegistration;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleLoader;

public class CoreExtension implements Extension {

    @Override
    public void initialize(ExtensionContext context) {
        log.info( "Initializing TorqueBox Core Subsystem" );
        final SubsystemRegistration registration = context.registerSubsystem( SUBSYSTEM_NAME );
        final ModelNodeRegistration subsystem = registration.registerSubsystemModel( CoreSubsystemProviders.SUBSYSTEM );

        subsystem.registerOperationHandler( ADD,
                CoreSubsystemAdd.ADD_INSTANCE,
                CoreSubsystemProviders.SUBSYSTEM_ADD,
                false );

        ModelNodeRegistration injector = subsystem.registerSubModel( PathElement.pathElement( "injector" ), CoreSubsystemProviders.INJECTOR );

        injector.registerOperationHandler( "add",
                InjectableHandlerAdd.ADD_INSTANCE,
                CoreSubsystemProviders.INJECTOR_ADD,
                false );

        registration.registerXMLElementWriter( CoreSubsystemParser.getInstance() );

        relink();

    }

    protected void relink() {
        Module module = Module.forClass( CoreExtension.class );
        ModuleLoader moduleLoader = module.getModuleLoader();

        try {
            Method method = ModuleLoader.class.getDeclaredMethod( "relink", Module.class );
            method.setAccessible( true );
            method.invoke( moduleLoader, module );
        } catch (SecurityException e) {
            log.fatal( e.getMessage(), e );
        } catch (NoSuchMethodException e) {
            log.fatal( e.getMessage(), e );
        } catch (IllegalArgumentException e) {
            log.fatal( e.getMessage(), e );
        } catch (IllegalAccessException e) {
            log.fatal( e.getMessage(), e );
        } catch (InvocationTargetException e) {
            log.fatal( e.getMessage(), e );
        }
    }

    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping( Namespace.CURRENT.getUriString(), CoreSubsystemParser.getInstance() );
    }

    public static final String SUBSYSTEM_NAME = "torquebox-core";
    static final Logger log = Logger.getLogger( "org.torquebox.core.as" );

}
