package org.nakedobjects.viewer.skylark;

import org.nakedobjects.utility.NotImplementedException;

/**
 * Details a drag event that is internal to view.
 */
public class InternalDrag extends Drag {

    public static InternalDrag create(View source, Location locationWithinViewer, Location locationWithinView, int modifiers) {
        InternalDrag drag = new InternalDrag(source, locationWithinViewer, locationWithinView, modifiers);

        return drag;
    }

    private final Location mouseLocation;
    private final Location originalMouseLocation;
    private final Location viewOffset;
 
    private final View overlay;

    /**
     * Creates a new drag event. The source view has its pickup(), and then,
     * exited() methods called on it. The view returned by the pickup method
     * becomes this event overlay view, which is moved continuously so that it
     * tracks the pointer,
     * 
     * @param source
     *                       the view over which the pointer was when this event started
     * @param mouseLocation
     *                       the location within the viewer (the Frame/Applet/Window etc)
     * @param locationWithinView
     *                       the location within the specified view
     * @param modifiers
     *                       the button and key modifiers (@see java.awt.event.MouseEvent)
     */
    private InternalDrag(View source, Location mouseLocation, Location locationWithinView, int modifiers) {
        super(source, mouseLocation, locationWithinView, modifiers);
        this.mouseLocation = new Location(mouseLocation);
        this.originalMouseLocation = new Location(mouseLocation);
        viewOffset = source.getAbsoluteLocation();
        overlay = source.dragFrom(this);
    }

    public void cancel() {
        view.dragCancel(this);
    }

    protected void drag() {
        view.drag(this);
    }

    protected void end() {
        view.dragTo(this);
    }

    /**
     * Returns the view that is shown in the overlay to provide feedback about
     * the drag actions..
     */
    public View getDragOverlay() {
        return overlay;
    }

    public Location getMouseLocation() {
        return mouseLocation;
    }
    
    /**
     * Offset from the location of the mouse when the drag started.
     */
    public Offset getOffset() {
        return mouseLocation.offsetFrom(originalMouseLocation);
    }
    
    public Location getRelativeLocation() {
        Location location = new Location(mouseLocation);
        location.subtract(viewOffset);
        return location;
    }

    public View getView() {
        return view;
    }

    public void subtract(int x, int y) {
        mouseLocation.subtract(x, y);
    }

    public String toString() {
        return "InternalDrag [location=" + mouseLocation + ",relative=" + getRelativeLocation() + ",offset=" + viewOffset + "," + super.toString() + "]";
    }

    // TODO remove
    public Offset totalMovement() {
        throw new NotImplementedException();
//        return new Offset(originalLocationWithinView.getX() - mouseLocation.getX(), originalLocationWithinView.getY() - mouseLocation.getY());
    }

    void updateLocationWithinViewer(Location mouseLocation, View target, Location locationWithinView) {
        this.mouseLocation.x = mouseLocation.x;
        this.mouseLocation.y = mouseLocation.y;
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2003 Naked Objects Group
 * Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address
 * of Naked Objects Group is Kingsway House, 123 Goldworth Road, Woking GU21
 * 1NR, UK).
 */