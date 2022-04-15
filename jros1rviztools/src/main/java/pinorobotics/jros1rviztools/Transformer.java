/*
 * Copyright 2022 jrosrviztools project
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
package pinorobotics.jros1rviztools;

import id.jrosmessages.ros1.visualization_msgs.MarkerMessage;
import pinorobotics.jrosrviztools.entities.MarkerType;

/** @author aeon_flux aeon_flux@eclipso.ch */
public class Transformer extends pinorobotics.jrosrviztools.Transformer {

    public MarkerMessage.Type toMarkerType(MarkerType markerType) {
        switch (markerType) {
            case ARROW:
                return MarkerMessage.Type.ARROW;
            case CUBE:
                return MarkerMessage.Type.CUBE;
            case SPHERE:
                return MarkerMessage.Type.SPHERE;
            case CYLINDER:
                return MarkerMessage.Type.CYLINDER;
            default:
                throw new IllegalArgumentException("Unexpected value: " + markerType);
        }
    }
}
