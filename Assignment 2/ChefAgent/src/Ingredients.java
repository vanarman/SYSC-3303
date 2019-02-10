/**
 * Enum class that contains ingredients for the sandwich
 *
 * @author Dmytro Sytnik (VanArman)
 * @version 05 February 2019
 */

public enum Ingredients {
    BREAD {
        @Override
        public String toString() {
            return "BREAD";
        }
    },
    PEANUT_BUTTER {
        @Override
        public String toString() {
            return "PEANUT_BUTTER";
        }
    },
    JAM {
        @Override
        public String toString() {
            return "JAM";
        }
    }
}
