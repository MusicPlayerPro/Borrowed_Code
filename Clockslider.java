        /* Display modes */
        public static final int CLOCK_SLIDER = 1;
        public static final int VOLUME_SLIDER = 2;
        public static final int VIBRATE_PICKER = 3;

        public static final boolean ENABLE_VIBRATE = false;
        private static final int INSETS = 6;
        private static final int MINUTES_PER_HALF_DAY = 100;

        private int width;
        private int height;
        private int centerX;
        private int centerY;
        private int diameter;
        private RectF innerCircle;
        private int displayMode = CLOCK_SLIDER;

        private Calendar start = new GregorianCalendar();
        private int startAngle = 90;
        private Calendar end = new GregorianCalendar();

        /** minutes to shush. */
        private int minutes = 0;

        private Bitmap bgBitmap;
        private Bitmap fgBitmap;
        private Path clipPath = new Path();

        private DialModel model;
        private float luftRotation = 0.0f;
        private int totalNicks = 100;
        private int currentNick = 0;

        public ClockSlider(Context context, AttributeSet attrs) {
            super(context, attrs);

            bgBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.black_circle);
            fgBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.blue_circle);
            setModel(new DialModel());
        }

        public final void setModel(DialModel model) {
            if (this.model != null) {
                this.model.removeListener(this);
            }
            this.model = model;
            this.model.addListener(this);

            invalidate();
        }

        public final DialModel getModel() {
            return model;
        }



        public Date getStart() {
            return start.getTime();
        }



        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            if (minutes == this.minutes) {
                return; // avoid unnecessary repaints
            }
            this.minutes = minutes;
            end.setTimeInMillis(start.getTimeInMillis() + (this.minutes * 60 * 1000L));
            postInvalidate();
        }

    //  public final float getRotationInDegrees() {
    //      return (360.0f / totalNicks) * currentNick;
    //  }

        public Date getEnd() {
            return end.getTime();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (getWidth() != width || getHeight() != height) {
                width = bgBitmap.getWidth();
                height = bgBitmap.getHeight();
    //          width = getWidth();
    //          height = getHeight();
                centerX = width / 2;
                centerY = height / 2;

                diameter = Math.min(width, height) - (2 * INSETS);
                int thickness = diameter / 15;

                int left = (width - diameter) / 2;
                int top = (height - diameter) / 2;
                int bottom = top + diameter;
                int right = left + diameter;
    //          outerCircle = new RectF(left, top, right, bottom);
                int innerDiameter = diameter - thickness * 2;
    //          innerCircle = new RectF(left + thickness, top + thickness, left
    //           + thickness + innerDiameter, top + thickness + innerDiameter);

                innerCircle = new RectF(0, 0,width,height);
                canvas.drawBitmap(bgBitmap, null, innerCircle, null);
            }

            if (displayMode == CLOCK_SLIDER) {
                drawClock(canvas);
            } else {
                throw new AssertionError();
            }
        }
        /**
         * Draw a circle and an arc of the selected duration from start thru end.
         */
        private void drawClock(Canvas canvas) {
            int sweepDegrees = (minutes / 2) - 1;

            canvas.drawBitmap(bgBitmap, null, innerCircle, null);
            // the colored "filled" part of the circle
            drawArc(canvas, startAngle, sweepDegrees);

        }

        @Override
        public void onDialPositionChanged(DialModel sender, int nicksChanged) {
            luftRotation = (float) (Math.random() * 1.0f - 0.5f);
            invalidate();
        }

        private void drawArc(Canvas canvas, int startAngle, int sweepDegrees) {
            if (sweepDegrees <= 0) {
                return;
            }
            clipPath.reset();
            clipPath.moveTo(innerCircle.centerX(), innerCircle.centerY());
            clipPath.arcTo(innerCircle, startAngle + sweepDegrees, -sweepDegrees);
    //      clipPath.lineTo(getWidth() / 2, getHeight() / 2);
            canvas.clipPath(clipPath);

            canvas.drawBitmap(fgBitmap, null, innerCircle, null);

            invalidate();
        }

        /**
         * Accept a touches near the circle's edge, translate it to an angle, and
         * update the sweep angle.
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int touchX = (int) event.getX();
            int touchY = (int) event.getY();
            int newDisplayMode = displayMode;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                newDisplayMode = CLOCK_SLIDER;
            }

            int distanceFromCenterX = centerX - touchX;
            int distanceFromCenterY = centerY - touchY;
            int distanceFromCenterSquared = distanceFromCenterX * distanceFromCenterX + distanceFromCenterY * distanceFromCenterY;
            float maxSlider = (diameter * 1.3f) / 2;
            float maxUpDown = (diameter * 0.8f) / 2;

            /*
             * Convert the angle into a sweep angle. The sweep angle is a positive
             * angle between the start angle and the touched angle.
             */
    //       if (distanceFromCenterSquared < (maxSlider * maxSlider)) {
            float x1 = bgBitmap.getWidth(), x2 = bgBitmap.getHeight(), y1 = bgBitmap
                    .getWidth(), y2 = bgBitmap.getHeight();
            if ((touchX <= x1 && touchX <= x2)  && (touchY <= y1 && touchY <= y2)) {
                int angle = pointToAngle(touchX, touchY);
                angle = 360 + angle - startAngle;
                int angleX2 = angle * 2;
                angleX2 = roundToNearest15(angleX2);
                if (angleX2 > 720) {
                    angleX2 = angleX2 - 720; // avoid mod because we prefer 720 over
                }
                if (angle <= 364) {
                    angleX2 = 0;
                }
    //          if (angleX2 > 720 || angleX2 < 400) {
    //              return false;
    //          }
                setMinutes(angleX2);
                model.rotate(Integer.valueOf("" + Math.round((angleX2 / 5.4))));
                return true;
            } else {

                return false;
            }
        }


        public int getProgress(){
            return minutes;
        }
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            // Don't use the full screen width on tablets!
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            float maxWidthInches = 2.3f;

            width = Math.min(width, (int) (maxWidthInches * metrics.densityDpi));
            height = Math.min(height, (int) (width * 0.7f));

            setMeasuredDimension(width, height);
        }

        /**
         * Returns the number of degrees (0-359) for the given point, such that 3pm
         * is 0 and 9pm is 180.
         */
        private int pointToAngle(int x, int y) {

            if (x >= centerX && y < centerY) {
                double opp = x - centerX;
                double adj = centerY - y;
                return 270 + (int) Math.toDegrees(Math.atan(opp / adj));
            } else if (x > centerX && y >= centerY) {
                double opp = y - centerY;
                double adj = x - centerX;
                return (int) Math.toDegrees(Math.atan(opp / adj));
            } else if (x <= centerX && y > centerY) {
                double opp = centerX - x;
                double adj = y - centerY;
                return 90 + (int) Math.toDegrees(Math.atan(opp / adj));
            } else if (x < centerX && y <= centerY) {
                double opp = centerY - y;
                double adj = centerX - x;
                return 180 + (int) Math.toDegrees(Math.atan(opp / adj));
            }

            throw new IllegalArgumentException();
        }

        /**
         * Rounds the angle to the nearest 7.5 degrees, which equals 15 minutes on a
         * clock. Not strictly necessary, but it discourages fat-fingered users from
         * being frustrated when trying to select a fine-grained period.
         */
        private int roundToNearest15(int angleX2) {
            return ((angleX2 + 8) / 15) * 15;
        }

    }