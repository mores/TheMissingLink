Step by Step guide 20240122 Ender3V2-422-BLTUBL-MPC
--------------------------------

Once you have installed your firmware, you can follow this guide.

![Control](../../images/control.png)
1. Control -> Restore Defaults

![Prepare](../../images/prepare.png)

2. Prepare -> Preheat (Pick your material)

	 wait until it reaches temperature

![TrammingWizard](../../images/trammingWizard.jpg)

3. Tramming Wizard

4. Z Proble Wizard

	Back

	Back

	![Advanced](../../images/advanced.png)

	Advanced Store Settings

5. Auto Build Mesh

	Save

![Advanced](../../images/advanced.png)

6. Advanced 

	MPC Settings

	MPC Autotune

	Back

	Store Settings

![Advanced](../../images/advanced.png)

7. Advanced

	Bed PID Settings

	PID Autotune

	Back

	Store Settings

** In slicer settings update
<pre>
G28 ; Home all axes
G29 L0 ; load a valid mesh from slot 0
G29 A  ; activate the UBL system
</pre>

# Happy Printing
