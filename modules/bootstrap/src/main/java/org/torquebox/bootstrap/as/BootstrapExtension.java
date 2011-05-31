package org.torquebox.bootstrap.as;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ModelNodeRegistration;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ResourceLoaderSpec;

public class BootstrapExtension implements Extension {

    @Override
    public void initialize(ExtensionContext context) {
        log.info( "Bootstrapping TorqueBox" );
        final SubsystemRegistration registration = context.registerSubsystem( SUBSYSTEM_NAME );
        final ModelNodeRegistration subsystem = registration.registerSubsystemModel( BootstrapSubsystemProviders.SUBSYSTEM );
        registration.registerXMLElementWriter( BootstrapSubsystemParser.getInstance() );

        File jrubyHome = determineJRubyHome();

        if (jrubyHome == null) {
            log.fatal( "Unable to find a JRuby Home" );
            return;
        }

        File libDir = new File( jrubyHome, "lib" );

        List<ResourceLoaderSpec> loaderSpecs = new ArrayList<ResourceLoaderSpec>();

        for (File child : libDir.listFiles()) {
            if (child.getName().endsWith( ".jar" )) {
                log.info( "Adding: " + child );
                try {
                    JarFileResourceLoader loader = new JarFileResourceLoader( child.getName(), new JarFile( child ) );
                    ResourceLoaderSpec loaderSpec = ResourceLoaderSpec.createResourceLoaderSpec( loader );
                    loaderSpecs.add( loaderSpec );
                } catch (IOException e) {
                    log.error( e );
                }
            }
        }

        swizzleDependencies( loaderSpecs );
    }

    private void swizzleDependencies(List<ResourceLoaderSpec> loaderSpecs) {

        Module module = Module.forClass( BootstrapExtension.class );
        ModuleLoader moduleLoader = module.getModuleLoader();

        try {
            Method method = ModuleLoader.class.getDeclaredMethod( "setAndRefreshResourceLoaders", Module.class, Collection.class );
            method.setAccessible( true );
            log.info( "Swizzle: " + loaderSpecs );
            method.invoke( moduleLoader, module, loaderSpecs );
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

    private File determineJRubyHome() {
        File jrubyHome = null;

        jrubyHome = ifExists( System.getProperty( "jruby.home" ) );

        if (jrubyHome != null) {
            return jrubyHome;
        }

        jrubyHome = ifExists( System.getenv( "JRUBY_HOME" ) );

        if (jrubyHome != null) {
            return jrubyHome;
        }

        File jbossHome = ifExists( System.getProperty( "jboss.home.dir" ) );

        if (jbossHome != null) {
            jrubyHome = new File( jbossHome.getParentFile(), "jruby" );
            if (jrubyHome.exists() && jrubyHome.isDirectory()) {
                return jrubyHome;
            }
        }

        return null;

    }

    private File ifExists(String path) {
        if (path == null) {
            return null;
        }

        if (path.trim().equals( "" )) {
            return null;
        }

        File file = new File( path );

        if (file.exists() && file.isDirectory()) {
            return file;
        }

        return null;
    }

    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping( Namespace.CURRENT.getUriString(), BootstrapSubsystemParser.getInstance() );
    }

    public static final String SUBSYSTEM_NAME = "torquebox-bootstrap";
    private static final Logger log = Logger.getLogger( "org.torquebox.bootstrap" );

}
