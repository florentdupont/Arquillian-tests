package fr.lynchmaniac.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * Classe gérant la construction de l'archive de déploiement
 * 
 */
public class Deployments {

    /**
     * Crée l'archive de déploiement
     */
    @Deployment
    public static WebArchive deploy() {
    
    	WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "ARQUILLIAN-TEST.war");
        
        webArchive.addPackages(true, "fr.lynchmaniac.arquilliantest")
                // dans ce cas, la classe de test doit être inclus dnas le WAR !
                .addClass(WeatherWsTest.class)
			    .addClass(WeatherWs2Test.class)
                .addAsWebInfResource("META-INF/ejb-jar.xml")
                .addAsWebInfResource("META-INF/beans.xml");
              
        
        
    	return webArchive;

    }
}
