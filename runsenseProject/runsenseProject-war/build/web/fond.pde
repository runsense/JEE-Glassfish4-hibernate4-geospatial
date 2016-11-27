float a=0.0;
                                void setup() 
                                {
                                    size(800, 600, P3D);
                                    background(0);
                                    PFont font = loadFont("Arial");
                                    textFont(font,70); 
                                } 

                                void draw(){

                                background(255);
                                a += 0.015;
                                if(a > TWO_PI) { 
                                    a = 0.0; 
                                }

                                translate(width/3, height/3,0);
                                rotateY(a);
                                //rotateY(a * 2.0);

                                //rect(5, 5, 20, 5);
                                fill(0, 102, 153);
                                text("Run$ense", 10, -20, 10);
                                //text("second");
                                fill(0, 102, 153);
                                text("$ense", 10, -20, -10); 

                                
                                fill(0, 102, 153);
                                text("RunSense", 30, 70, 10); 
                                fill(0, 102, 153);
                                text("Sense", 30, 70, -10);
                                }