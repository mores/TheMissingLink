1. fresh install Raspberry Pi OS (64-bit) trixie

2. git clone https://github.com/mores/TheMissingLink.git

3. cd TheMissingLink/Falcon_PiCap_V2/deviceTreeOverlay/

4. make

5. sudo make install

6. sudo vi /boot/firmware/config.txt

## Add to the bottom of the file

dtoverlay=vc4-kms-dpi-fpp-pi3,rgb888,hactive=362,hfp=0,hsync=1,hbp=0,vactive=162,vfp=1,vsync=1,vbp=1,status=okay

7. reboot

8. sudo systemctl stop lightdm

9. sudo apt install libbcm2835-dev libdrm-dev

10. cd TheMissingLink/Falcon_PiCap_V2/c/

11. make

12. ./pixel

Strand/Port 1 and 2 should now show:
	pixel0 - green
	pixel1 - red
	pixel2 - blue
