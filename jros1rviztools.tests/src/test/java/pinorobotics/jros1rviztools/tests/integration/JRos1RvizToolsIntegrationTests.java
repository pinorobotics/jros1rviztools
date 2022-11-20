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

import id.jros1client.JRos1ClientFactory;
import id.xfunction.logging.XLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pinorobotics.jros1rviztools.JRos1RvizToolsFactory;
import pinorobotics.jrosrviztools.entities.Color;
import pinorobotics.jrosrviztools.entities.MarkerType;
import pinorobotics.jrosrviztools.entities.Point;
import pinorobotics.jrosrviztools.entities.Pose;
import pinorobotics.jrosrviztools.entities.Scales;

/**
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public class JRos1RvizToolsIntegrationTests {

    @BeforeAll
    public static void setupAll() {
        XLogger.load("jrosrviztools-test.properties");
    }

    @Test
    public void test_example_from_documentation() throws Exception {
        var clientFactory = new JRos1ClientFactory();
        var rvizToolsFactory = new JRos1RvizToolsFactory();
        try (var client = clientFactory.createClient("http://127.0.0.1:11311/");
                var rvizTools =
                        rvizToolsFactory.createRvizTools(
                                client, "map", "/visualization_marker_array")) {
            rvizTools.publishText(
                    Color.RED, Scales.XLARGE, new Pose(new Point(0, 0, 1)), "Hello from Java");
            rvizTools.publishMarkers(
                    Color.RED, Scales.XLARGE, MarkerType.SPHERE, new Point(1, 0, 1));
            System.out.println("Press Enter to stop...");
            System.in.read();
        }
    }
}
