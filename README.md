# FTCCode
2026


__Auto Plan:__
1) Position the Robot with both wheels touching the plastic and aligned with a certain landmark
2) Robot moves forward a certain distance using the encoders and stops
3) Robot turns either left or right a certain angle depending on what alliance we are to face the goal
4) Robot moves forward or backwards optionally depending on our output's range
5) Output spins for a certain amount of time then stops
6) Robot turns back to original heading
7) Robot moves backwards a certain distance to clear the tape and get the points for that
End

__Teleop Code Gamepad 1:__
1) Mecanum wheels are driven with both the left and right joystick (left = Forward, Backward, Sideways; right = Spinning)
2) Robot will move straight forward at a speed determined on the value of the left trigger until red or blue is sensed. Parking is initiated by the left trigger

__Teleop Code Gamepad 2:__
1) Output will spin at a predetermined speed while button 'a' is _held_. Once it's released, the intake will stop. [There's also a continuous option]
2) Intake will start spinning at a predetermined speed once button 'a' is _pressed_ and will stop when button 'a' is pressed again [There's also a continuous option]
