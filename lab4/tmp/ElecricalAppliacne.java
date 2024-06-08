/**
 * The `lab4.tmp` package contains classes and interfaces related to electrical appliances,
 * computer systems, and their associated components.
 */
package lab4.tmp;

/**
 * The `ElecricalAppliacne` class represents a generic electrical appliance.
 * It has the following properties:
 * - `model`: the model of the appliance
 * - `manufacturer`: the manufacturer of the appliance
 * - `plugType`: the type of plug used by the appliance
 * It also provides methods to turn the appliance on and off:
 * - `turnOn()`: turns the appliance on
 * - `turnOff()`: turns the appliance off
 */
public class ElecricalAppliacne {
    String model;
    String manufacturer;
    Plug plugType;

    /**
     * Turns the electrical appliance on.
     */
    void turnOn() {}

    /**
     * Turns the electrical appliance off.
     */
    void turnOff() {}
}

