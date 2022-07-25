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

/**
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public interface TestConstants {

    String CALLER_ID = "jrosclient";
    String TOPIC = "testTopic";
    String URL = "http://127.0.0.1:11311/";
    int PORT = 1234;
    String RVIZ_MARKER_TOPIC = "/visualization_marker_array";
}
