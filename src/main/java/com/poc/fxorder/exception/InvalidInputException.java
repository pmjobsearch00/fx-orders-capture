/*
 * No Copyright intended or License applies just for templating.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.fxorder.exception;

/**
 * Class InvalidInputException
 * 
 * It is used for any invalid input parameter related exception
 * 
 * @author PM
 *
 */

public class InvalidInputException extends ServiceException {

	/**
	 * Serialization id.
	 */
	private static final long serialVersionUID = 1381320130022416598L;

	/**
	 * Constructor.
	 */
	public InvalidInputException(String message) {
		super(message);
	}
}
