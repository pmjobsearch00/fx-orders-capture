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
 * Class ServiceException
 * 
 * It is used for any unexpected service exception
 * 
 * @author PM
 *
 */
public class ServiceException extends RuntimeException {

	/**
	 * Serialization id.
	 */
    private static final long serialVersionUID = -8658131859261427602L;

    /** The message. */
    private String message;

    /**
	 * Constructor.
	 */
    public ServiceException(final String message) {
        super();
        this.message = message;
    }

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
