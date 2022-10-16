/*
 * Copyright 2021 jrosrviztools project
 * 
 * Website: https://github.com/pinorobotics/jros1rviztools
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pinorobotics.jros1rviztools.tests.integration;

import static pinorobotics.jros1rviztools.tests.integration.TestConstants.RVIZ_MARKER_TOPIC;
import static pinorobotics.jros1rviztools.tests.integration.TestConstants.URL;

import id.jros1client.JRos1ClientFactory;
import id.jrosclient.JRosClient;
import id.xfunction.logging.XLogger;
import java.net.MalformedURLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pinorobotics.jros1rviztools.JRos1RvizToolsFactory;
import pinorobotics.jrosrviztools.JRosRvizTools;
import pinorobotics.jrosrviztools.entities.Color;
import pinorobotics.jrosrviztools.entities.MarkerType;
import pinorobotics.jrosrviztools.entities.Point;
import pinorobotics.jrosrviztools.entities.Pose;
import pinorobotics.jrosrviztools.entities.Scales;

/**
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public class JRos1RvizToolsIntegrationTests {

    private static final JRos1ClientFactory clientFactory = new JRos1ClientFactory();
    private static final JRos1RvizToolsFactory toolsFactory = new JRos1RvizToolsFactory();
    private JRosClient client;
    private JRosRvizTools rvizTools;

    @BeforeAll
    public static void setupAll() {
        XLogger.load("jrosrviztools-test.properties");
    }

    @BeforeEach
    public void setup() throws MalformedURLException {
        client = clientFactory.createClient(URL);
        rvizTools = toolsFactory.createJRosRvizTools(client, "map", RVIZ_MARKER_TOPIC);
    }

    @AfterEach
    public void clean() throws Exception {
        rvizTools.close();
        client.close();
    }

    @Test
    public void test_all() throws Exception {
        rvizTools.publishText(
                Color.RED, Scales.XLARGE, new Pose(new Point(0, 0, 1)), "Hello from Java");
        rvizTools.publishMarkers(Color.RED, Scales.XLARGE, MarkerType.SPHERE, new Point(1, 0, 1));
    }
}
